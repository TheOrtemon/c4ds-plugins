package vision.combat.c4.ds.example.tool.polka_dots

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import vision.combat.c4.ds.sdk.domain.interactor.CommonModelInteractor
import vision.combat.c4.ds.sdk.domain.interactor.selectedModelUpdatedEvent
import vision.combat.c4.ds.sdk.domain.util.distanceTo
import vision.combat.c4.model.BattlespaceConceptModel
import vision.combat.c4.model.association.AssociationModel
import vision.combat.c4.model.association.AssociationType
import vision.combat.c4.model.location.Approximable
import vision.combat.c4.model.location.ArcLocation
import vision.combat.c4.model.location.ArrowLocation
import vision.combat.c4.model.location.GeoBox
import vision.combat.c4.model.location.GeoMath
import vision.combat.c4.model.location.GeoPoint
import vision.combat.c4.model.location.LineLocation
import vision.combat.c4.model.location.PointLocation
import vision.combat.c4.model.location.VisualAttributes
import vision.combat.c4.model.location.Volume
import vision.combat.c4.model.overlay.OverlayModel
import vision.combat.c4.model.randomId
import kotlin.uuid.ExperimentalUuidApi


internal class PolkaDotsToolViewModel(
    private val modelInteractor: CommonModelInteractor,
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        with(modelInteractor) {
            merge(selectedModel, selectedModelUpdatedEvent)
                .onEach { updateSelectedModel(it) }
                .launchIn(viewModelScope)
        }
    }

    private fun breakIntoPoints(
        stepValue: Double,
        prefix: String,
        color: Color,
        startingNumber: Int,
        model: BattlespaceConceptModel
    ) {
        val geoPoints: List<GeoPoint>? = when (model.geographicLocation) {
            is Volume -> {
                when (model.geographicLocation) {
                    is Approximable -> {
                        val boundingBox = model.geographicLocation.boundingBox
                        val polygon = (model.geographicLocation as Approximable).approximatePoints

                        generateGridInsidePolygon(polygon, boundingBox, stepValue)
                    }
                    else -> null
                }
            }
            is ArcLocation, is ArrowLocation, is LineLocation -> {
                breakApproximableIntoPoints(model.geographicLocation as Approximable, stepValue)
            }
            else -> {
                null
            }
        }
        if (!geoPoints.isNullOrEmpty()) {
            geoPointsToModels(modelInteractor, geoPoints, model, prefix, color, startingNumber)

        }
    }

    fun process() {
        val step = uiState.distance.toDoubleOrNull() ?: return
        val startingNumber = uiState.startingNumber.toIntOrNull() ?: return
        val model = modelInteractor.selectedModel.value ?: return

        val prefix = if (uiState.isPrefixFromModel) {
            model.name ?: ""
        } else {
            uiState.prefix
        }

        val color = if (uiState.isColorFromModel) {
            val colorInt = model.location.visualAttributes?.lineColor ?: 0
            Color(colorInt)
        } else {
            uiState.color
        }

        breakIntoPoints(step, prefix, color, startingNumber, model)
    }


    private fun breakApproximableIntoPoints(
        lineLocation: Approximable,
        segmentDistance: Double
    ): MutableList<GeoPoint> {
        val approximatePoints = lineLocation.approximatePoints
        val resultPoints = mutableListOf(approximatePoints.first())

        var completedDistanceInSegment = 0.0

        for (i in 1 until approximatePoints.size) {
            var startPoint = approximatePoints[i - 1]
            val endPoint = approximatePoints[i]
            var distanceToEnd = startPoint.distanceTo(endPoint)

            while (distanceToEnd >= segmentDistance - completedDistanceInSegment) {
                val remainingDistance = segmentDistance - completedDistanceInSegment
                val proportionalPoint = GeoMath.getProportionalPoint(
                    startPoint,
                    endPoint,
                    remainingDistance / distanceToEnd
                )

                resultPoints.add(proportionalPoint)
                startPoint = proportionalPoint
                distanceToEnd = proportionalPoint.distanceTo(endPoint)
                completedDistanceInSegment = 0.0
            }

            completedDistanceInSegment += distanceToEnd
        }
        return resultPoints
    }

    private fun generateGridInsidePolygon(
        polygon: List<GeoPoint>,
        bounds: GeoBox,
        step: Double,
    ): List<GeoPoint> {
        val gridPoints = mutableListOf<GeoPoint>()
        var currentLat = bounds.bottomLeft

        while (currentLat.lat <= bounds.topRight.lat) {
            var currentPoint = currentLat

            while (currentPoint.lon <= bounds.topRight.lon) {
                if (isPointInPolygon(currentPoint, polygon)) {
                    gridPoints.add(currentPoint)
                }

                currentPoint = GeoMath.calculatePoint(currentPoint, 90.0, step)
                if (currentPoint.lon <= currentPoint.lon - 0.0001) break
            }

            currentLat = GeoMath.calculatePoint(currentLat, 0.0, step)
            if (currentLat.lat <= currentLat.lat - 0.0001) break
        }

        return gridPoints
    }

    private fun isPointInPolygon(point: GeoPoint, polygon: List<GeoPoint>): Boolean {
        if (polygon.size < 3) return false

        var crossings = 0

        for (i in polygon.indices) {
            val j = (i + 1) % polygon.size

            val xi = polygon[i].lon
            val yi = polygon[i].lat
            val xj = polygon[j].lon
            val yj = polygon[j].lat

            if (yi == yj) continue
            if ((yi > point.lat) == (yj > point.lat)) continue

            val intersectionX = xi + (xj - xi) * (point.lat - yi) / (yj - yi)

            if (intersectionX > point.lon) {
                crossings++
            }
        }

        return crossings % 2 == 1
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun geoPointsToModels(
        modelInteractor: CommonModelInteractor,
        geoPoints: List<GeoPoint>,
        model: BattlespaceConceptModel,
        prefix: String,
        color: Color,
        startingNumber: Int
    ) {
        val pointsToIterate = if (uiState.isReversedOrder) geoPoints.reversed() else geoPoints

        pointsToIterate.forEachIndexed { index, point ->
            val newPointModel = BattlespaceConceptModel(
                randomId(),
                geographicLocation = PointLocation(point).apply {
                    visualAttributes = VisualAttributes(
                        lineColor = color.toArgb(),
                        fillColor = color.toArgb()
                    )
                }
            ).apply {
                name = "$prefix ${startingNumber + index + 1}"
                symbolKey = 2045
            }
            model.container.forEach {
                modelInteractor.consumeModel(
                    AssociationModel(
                        randomId(),
                        it.id,
                        newPointModel.id,
                        AssociationType.OverlayIncludesThing
                    ))
            }
            modelInteractor.consumeModel(
                newPointModel,
                attachToDefault = true
            )
        }
        if (uiState.isModelDeleted) {
            modelInteractor.deleteModel(model)
        }
        modelInteractor.commitChanges()
    }

    fun layerToModels(layer: OverlayModel) {
        val step = uiState.distance.toDoubleOrNull() ?: return
        val startingNumber = uiState.startingNumber.toIntOrNull() ?: return

        layer.content.forEach {
            val prefix = if (uiState.isPrefixFromModel) {
                it.name ?: ""
            } else {
                uiState.prefix
            }

            val color = if (uiState.isColorFromModel) {
                val colorInt = it.location.visualAttributes?.lineColor ?: 0
                Color(colorInt)
            } else {
                uiState.color
            }

            breakIntoPoints(step, prefix, color, startingNumber, it)
        }
    }

    data class UiState(
        val prefix: String = "",
        val distance: String = "100",
        val startingNumber: String = "0",
        val color: Color = Color.Black,
        val isReversedOrder: Boolean = false,
        val isColorFromModel: Boolean = false,
        val isPrefixFromModel: Boolean = false,
        val isModelDeleted: Boolean = false,
        val selectedModel: BattlespaceConceptModel? = null,
    )

    fun updatePrefix(newPrefix: String) {
        uiState = uiState.copy(prefix = newPrefix)
    }

    fun updateDistance(newDistance: String) {
        uiState = uiState.copy(distance = newDistance)
    }

    fun updateStartingNumber(newStartingNumber: String) {
        uiState = uiState.copy(startingNumber = newStartingNumber)
    }

    fun updateColor(newColor: Color) {
        uiState = uiState.copy(color = newColor)
    }

    fun updateIsReversedOrder(newIsReversedOrder: Boolean) {
        uiState = uiState.copy(isReversedOrder = newIsReversedOrder)
    }
    
    fun updateIsColorFromModel(newIsColorFromModel: Boolean) {
        uiState = uiState.copy(isColorFromModel = newIsColorFromModel)
    }

    fun updateIsPrefixFromModel(newIsPrefixFromModel: Boolean) {
        uiState = uiState.copy(isPrefixFromModel = newIsPrefixFromModel)
    }

    fun updateIsModelRetained(newIsModelRetained: Boolean) {
        uiState = uiState.copy(isModelDeleted = newIsModelRetained)
    }

    fun updateSelectedModel(selectedModel: BattlespaceConceptModel?) {
        uiState = uiState.copy(selectedModel = selectedModel)
    }
}

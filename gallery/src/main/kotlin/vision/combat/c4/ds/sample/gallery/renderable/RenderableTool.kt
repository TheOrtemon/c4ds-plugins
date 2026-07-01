package vision.combat.c4.ds.sample.gallery.renderable

import earth.worldwind.geom.AltitudeMode
import earth.worldwind.geom.Location
import earth.worldwind.geom.Offset
import earth.worldwind.geom.Position
import earth.worldwind.render.Renderable
import earth.worldwind.shape.Ellipse
import earth.worldwind.shape.Label
import earth.worldwind.shape.Path
import earth.worldwind.shape.PathType
import earth.worldwind.shape.Placemark
import earth.worldwind.shape.Polygon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.renderable.ui.RenderableColor
import vision.combat.c4.ds.sample.gallery.renderable.ui.RenderableControlsWindow
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.tool.AbstractMapTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent
import earth.worldwind.render.Color as WWColor

/**
 * Minimal AbstractMapTool wiring a RenderableControlsWindow and a managed WorldWind renderable
 * layer (addRenderable/removeRenderable). Seeds one Placemark, Path, Polygon, Ellipse and Label
 * at the Kyiv demo center on construction.
 */
internal class RenderableTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractMapTool(toolContext, toolDescriptor, parentDI, params) {

    private val mapInteractor: CommonMapInteractor by instance()

    // Keep references to everything we add so clearAll() can remove each renderable.
    private val renderables = mutableListOf<Renderable>()

    private val _addedCount = MutableStateFlow(0)
    val addedCount = _addedCount.asStateFlow()

    /** Currently selected color applied to newly added shapes. */
    private val _selectedColor = MutableStateFlow(RenderableColor.CYAN)
    val selectedColor = _selectedColor.asStateFlow()

    override val window: ToolComponent.Window by requiredComponent {
        RenderableControlsWindow(
            addedCount = addedCount,
            selectedColor = selectedColor,
            onColorSelected = { _selectedColor.value = it },
            onAddPoint = ::addPoint,
            onAddLine = ::addLine,
            onAddPolygon = ::addPolygon,
            onAddCircle = ::addCircle,
            onAddLabel = ::addLabel,
            onClear = ::clearAll,
        )
    }

    init {
        // Seed one of each shape at the Kyiv demo center, then frame the camera on it.
        val center = Position.fromDegrees(CENTER_LAT, CENTER_LON, 0.0)
        addPointAt(center)
        addLineAt(center)
        addPolygonAt(center)
        addCircleAt(center)
        addLabelAt(center)
        mapInteractor.focusOnLocation(Location.fromDegrees(CENTER_LAT, CENTER_LON))
    }

    /** Adds a point placemark at the live cursor position. */
    fun addPoint() = addPointAt(mapInteractor.selectedPosition.value)

    /** Adds a polyline centred on the live cursor position. */
    fun addLine() = addLineAt(mapInteractor.selectedPosition.value)

    /** Adds a polygon centred on the live cursor position. */
    fun addPolygon() = addPolygonAt(mapInteractor.selectedPosition.value)

    /** Adds a circle centred on the live cursor position. */
    fun addCircle() = addCircleAt(mapInteractor.selectedPosition.value)

    /** Adds a text label at the live cursor position. */
    fun addLabel() = addLabelAt(mapInteractor.selectedPosition.value)

    private fun addPointAt(pos: Position) {
        track(
            Placemark(Position.fromDegrees(pos.latitude.inDegrees, pos.longitude.inDegrees, 0.0)).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                attributes.apply {
                    imageScale = 2.0
                    imageOffset = Offset.bottomCenter()
                }
            }
        )
    }

    private fun addLineAt(pos: Position) {
        val lat = pos.latitude.inDegrees
        val lon = pos.longitude.inDegrees
        val wwColor = WWColor(_selectedColor.value.androidColor)
        track(
            Path(
                listOf(
                    Position.fromDegrees(lat - LINE_SPAN, lon - LINE_SPAN, 0.0),
                    Position.fromDegrees(lat, lon, 0.0),
                    Position.fromDegrees(lat + LINE_SPAN, lon - LINE_SPAN, 0.0),
                )
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isFollowTerrain = true
                pathType = PathType.LINEAR
                isPickEnabled = false
                attributes.apply {
                    isDrawInterior = false
                    outlineColor = wwColor
                    outlineWidth = 4f
                }
            }
        )
    }

    private fun addPolygonAt(pos: Position) {
        val lat = pos.latitude.inDegrees
        val lon = pos.longitude.inDegrees
        val wwColor = WWColor(_selectedColor.value.androidColor)
        track(
            // Outer ring, do NOT repeat the first point — WorldWind closes the polygon automatically.
            Polygon(
                listOf(
                    Position.fromDegrees(lat + POLY_SPAN, lon - POLY_SPAN, 0.0),
                    Position.fromDegrees(lat + POLY_SPAN, lon + POLY_SPAN, 0.0),
                    Position.fromDegrees(lat - POLY_SPAN, lon + POLY_SPAN, 0.0),
                    Position.fromDegrees(lat - POLY_SPAN, lon - POLY_SPAN, 0.0),
                )
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isFollowTerrain = true
                pathType = PathType.LINEAR
                isPickEnabled = false
                attributes.apply {
                    outlineColor = wwColor
                    outlineWidth = 2f
                    interiorColor = WWColor(_selectedColor.value.androidColor).apply { alpha = 0.15f }
                }
            }
        )
    }

    private fun addCircleAt(pos: Position) {
        val wwColor = WWColor(_selectedColor.value.androidColor)
        track(
            // Radii are in meters; equal major/minor radii produce a circle.
            Ellipse(
                Position.fromDegrees(pos.latitude.inDegrees, pos.longitude.inDegrees, 0.0),
                CIRCLE_RADIUS_M,
                CIRCLE_RADIUS_M,
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isFollowTerrain = true
                isPickEnabled = false
                attributes.apply {
                    isDrawInterior = false
                    outlineColor = wwColor
                    outlineWidth = 2f
                }
            }
        )
    }

    private fun addLabelAt(pos: Position) {
        val wwColor = WWColor(_selectedColor.value.androidColor)
        track(
            Label(
                Position.fromDegrees(pos.latitude.inDegrees, pos.longitude.inDegrees, 0.0),
                LABEL_TEXT,
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isPickEnabled = false
                attributes.apply {
                    textColor = wwColor
                    scale = 1.2
                    textOffset = Offset.center()
                    isOutlineEnabled = true
                    outlineColor = WWColor(0f, 0f, 0f, 1f)
                }
            }
        )
    }

    fun clearAll() {
        renderables.forEach { removeRenderable(it) }
        renderables.clear()
        _addedCount.value = 0
        mapInteractor.requestRedraw()
    }

    // AbstractMapTool.onTerrainPicked takes a single Position; addRenderable() uses the managed layer.
    override fun onTerrainPicked(position: Position) {
        super.onTerrainPicked(position)
        track(
            Placemark(position).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                attributes.apply {
                    imageScale = 2.0
                    imageOffset = Offset.bottomCenter()
                }
            }
        )
    }

    private fun track(renderable: Renderable) {
        renderables.add(renderable)
        addRenderable(renderable)
        _addedCount.value = renderables.size
        mapInteractor.requestRedraw()
    }

    private companion object {
        const val CENTER_LAT = 50.45
        const val CENTER_LON = 30.52

        const val LINE_SPAN = 0.006
        const val POLY_SPAN = 0.004
        const val CIRCLE_RADIUS_M = 400.0
        const val LABEL_TEXT = "Kyiv"
    }
}

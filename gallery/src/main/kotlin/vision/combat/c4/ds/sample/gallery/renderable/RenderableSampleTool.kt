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
import vision.combat.c4.ds.sample.gallery.renderable.ui.RenderableControls
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.tool.AbstractMapTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.statusComponent
import earth.worldwind.render.Color as WWColor

internal class RenderableSampleTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractMapTool(toolContext, toolDescriptor, parentDI, params) {

    private val mapInteractor: CommonMapInteractor by instance()

    // Keep references to everything we add so clearAll() can remove each renderable.
    private val renderables = mutableListOf<Renderable>()

    // Monotonic counter used to nudge each new shape so successive adds do not overlap.
    private var addCount = 0

    private val _addedCount = MutableStateFlow(0)
    val addedCount = _addedCount.asStateFlow()

    override val status: ToolComponent.Status by statusComponent(isDefault = true) {
        RenderableControls(
            addedCount = addedCount,
            onAddPoint = ::addPoint,
            onAddLine = ::addLine,
            onAddPolygon = ::addPolygon,
            onAddCircle = ::addCircle,
            onAddLabel = ::addLabel,
            onClear = ::clearAll,
        )
    }

    init {
        // Seed one of each shape around the demo center, then frame the camera on it.
        addPoint()
        addLine()
        addPolygon()
        addCircle()
        addLabel()
        mapInteractor.focusOnLocation(Location.fromDegrees(CENTER_LAT, CENTER_LON))
    }

    fun addPoint() {
        val center = nextCenter()
        track(
            Placemark(Position.fromDegrees(center.latitude.inDegrees, center.longitude.inDegrees, 0.0)).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                attributes.apply {
                    imageScale = 2.0
                    imageOffset = Offset.bottomCenter()
                }
            }
        )
    }

    fun addLine() {
        val center = nextCenter()
        val lat = center.latitude.inDegrees
        val lon = center.longitude.inDegrees
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
                    outlineColor = WWColor(android.graphics.Color.CYAN)
                    outlineWidth = 4f
                }
            }
        )
    }

    fun addPolygon() {
        val center = nextCenter()
        val lat = center.latitude.inDegrees
        val lon = center.longitude.inDegrees
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
                    outlineColor = WWColor(android.graphics.Color.YELLOW)
                    outlineWidth = 2f
                    interiorColor = WWColor(android.graphics.Color.YELLOW).apply { alpha = 0.15f }
                }
            }
        )
    }

    fun addCircle() {
        val center = nextCenter()
        track(
            // Radii are in meters; equal major/minor radii produce a circle.
            Ellipse(
                Position.fromDegrees(center.latitude.inDegrees, center.longitude.inDegrees, 0.0),
                CIRCLE_RADIUS_M,
                CIRCLE_RADIUS_M,
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isFollowTerrain = true
                isPickEnabled = false
                attributes.apply {
                    isDrawInterior = false
                    outlineColor = WWColor(android.graphics.Color.RED)
                    outlineWidth = 2f
                }
            }
        )
    }

    fun addLabel() {
        val center = nextCenter()
        track(
            Label(
                Position.fromDegrees(center.latitude.inDegrees, center.longitude.inDegrees, 0.0),
                LABEL_TEXT,
            ).apply {
                altitudeMode = AltitudeMode.CLAMP_TO_GROUND
                isPickEnabled = false
                attributes.apply {
                    textColor = WWColor(android.graphics.Color.WHITE)
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

    /**
     * Returns the next shape center, nudged by a deterministic per-add delta so successive shapes
     * fan out diagonally instead of stacking on top of each other.
     */
    private fun nextCenter(): Location {
        val offset = addCount++ * STEP_DEGREES
        return Location.fromDegrees(CENTER_LAT + offset, CENTER_LON + offset)
    }

    private companion object {
        const val CENTER_LAT = 50.45
        const val CENTER_LON = 30.52

        // Per-add diagonal nudge so shapes do not overlap.
        const val STEP_DEGREES = 0.004

        const val LINE_SPAN = 0.006
        const val POLY_SPAN = 0.004
        const val CIRCLE_RADIUS_M = 400.0
        const val LABEL_TEXT = "Kyiv"
    }
}

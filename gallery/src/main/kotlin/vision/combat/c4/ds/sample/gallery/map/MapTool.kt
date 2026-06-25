package vision.combat.c4.ds.sample.gallery.map

import earth.worldwind.geom.Position
import earth.worldwind.gesture.GestureRecognizer
import earth.worldwind.render.RenderableLayer
import earth.worldwind.shape.Placemark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI
import vision.combat.c4.ds.sample.gallery.map.ui.MapStatusBar
import vision.combat.c4.ds.sdk.tool.AbstractMapTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.renderableMapLayer
import vision.combat.c4.ds.sdk.tool.statusComponent

internal class MapTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractMapTool(toolContext, toolDescriptor, parentDI, params) {

    private val _lastTap = MutableStateFlow<String?>(null)
    val lastTap = _lastTap.asStateFlow()

    private val layer: RenderableLayer by renderableMapLayer()

    override val status: ToolComponent.Status by statusComponent {
        MapStatusBar(lastTap)
    }

    override fun onTerrainPicked(
        recognizer: GestureRecognizer,
        position: Position,
        pickList: earth.worldwind.pick.PickedObjectList,
    ) {
        super.onTerrainPicked(recognizer, position, pickList)
        _lastTap.value = "${position.latitude.degrees}°, ${position.longitude.degrees}°"
        val placemark = Placemark(position)
        layer.addRenderable(placemark)
        mapInteractor.requestRedraw()
    }
}


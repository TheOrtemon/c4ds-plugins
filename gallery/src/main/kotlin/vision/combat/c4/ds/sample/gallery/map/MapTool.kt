package vision.combat.c4.ds.sample.gallery.map

import earth.worldwind.geom.Position
import earth.worldwind.shape.Placemark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI
import org.kodein.di.instance
import vision.combat.c4.ds.sample.gallery.map.ui.MapStatusBar
import vision.combat.c4.ds.sdk.domain.interactor.CommonMapInteractor
import vision.combat.c4.ds.sdk.tool.AbstractMapTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.statusComponent

internal class MapTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractMapTool(toolContext, toolDescriptor, parentDI, params) {

    private val mapInteractor: CommonMapInteractor by instance()

    private val _lastTap = MutableStateFlow<String?>(null)
    val lastTap = _lastTap.asStateFlow()

    override val status: ToolComponent.Status by statusComponent(isDefault = true) {
        MapStatusBar(lastTap)
    }

    // AbstractMapTool.onTerrainPicked takes a single Position argument.
    // addRenderable() is provided by AbstractMapTool and uses its managed layer.
    override fun onTerrainPicked(position: Position) {
        super.onTerrainPicked(position)
        _lastTap.value = "%.4f°, %.4f°".format(
            position.latitude.inDegrees,
            position.longitude.inDegrees,
        )
        addRenderable(Placemark(position))
        mapInteractor.requestRedraw()
    }
}

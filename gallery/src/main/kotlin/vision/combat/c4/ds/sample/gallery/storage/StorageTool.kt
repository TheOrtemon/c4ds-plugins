package vision.combat.c4.ds.sample.gallery.storage

import org.kodein.di.DI
import org.kodein.di.subDI
import vision.combat.c4.ds.sample.gallery.storage.di.storageModule
import vision.combat.c4.ds.sample.gallery.storage.ui.StorageWindow
import vision.combat.c4.ds.sdk.tool.AbstractTool
import vision.combat.c4.ds.sdk.tool.ToolComponent
import vision.combat.c4.ds.sdk.tool.ToolContext
import vision.combat.c4.ds.sdk.tool.ToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolParams
import vision.combat.c4.ds.sdk.tool.requiredComponent

/**
 * Minimal [AbstractTool] subclass for the storage showcase hub; imports [storageModule]
 * and wires [StorageWindow] as the single window component.
 */
internal class StorageTool(
    toolContext: ToolContext,
    toolDescriptor: ToolDescriptor,
    parentDI: DI,
    params: ToolParams?,
) : AbstractTool(toolContext, toolDescriptor, parentDI, params) {

    override val di: DI = subDI(super.di) { import(storageModule) }

    override val window: ToolComponent.Window by requiredComponent {
        StorageWindow()
    }
}

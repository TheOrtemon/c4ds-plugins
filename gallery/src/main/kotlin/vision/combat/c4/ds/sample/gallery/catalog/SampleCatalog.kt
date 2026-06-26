package vision.combat.c4.ds.sample.gallery.catalog

import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.dialog.DialogToolDescriptor
import vision.combat.c4.ds.sample.gallery.endbar.EndBarSampleToolDescriptor
import vision.combat.c4.ds.sample.gallery.expandablestatus.ExpandableStatusToolDescriptor
import vision.combat.c4.ds.sample.gallery.map.MapToolDescriptor
import vision.combat.c4.ds.sample.gallery.mapinteractor.MapInteractorToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.map.MapWindowToolDescriptor
import vision.combat.c4.ds.sample.gallery.model.ModelToolDescriptor
import vision.combat.c4.ds.sample.gallery.overlay.OverlaySampleToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.collision.CollisionToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.config.ConfigToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.material.MaterialToolDescriptor
import vision.combat.c4.ds.sample.gallery.service.ServiceToolDescriptor
import vision.combat.c4.ds.sample.gallery.status.StatusToolDescriptor
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogToolDescriptor
import vision.combat.c4.ds.sample.gallery.underlay.UnderlayToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.singlescreen.WindowSingleScreenToolDescriptor
import vision.combat.c4.ds.sdk.tool.ToolManager
import vision.combat.c4.ds.sdk.tool.activate

/**
 * Registry of all gallery samples.
 *
 * Each entry specifies its section, display strings, and a launch lambda.
 * For same-APK tools the reified activate<T> overload is used.
 * For the cross-APK :isolation tool, resolveToolId(fqcn) is used.
 */
object SampleCatalog {

    private const val ISOLATION_NATIVE_FQCN =
        "vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor"

    val entries: List<SampleEntry> = listOf(

        // ── WINDOWS ─────────────────────────────────────────────────────────
        SampleEntry(
            id = "window_single_screen",
            section = SampleSection.WINDOWS,
            nameResId = R.string.window_single_screen_tool_name,
            descResId = R.string.window_single_screen_desc,
            apisResId = R.string.window_single_screen_apis,
            sourceSubpackage = "window/singlescreen",
            launch = { mgr -> mgr.activate<WindowSingleScreenToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "window_multi_screen",
            section = SampleSection.WINDOWS,
            nameResId = R.string.window_multi_screen_tool_name,
            descResId = R.string.window_multi_screen_desc,
            apisResId = R.string.window_multi_screen_apis,
            sourceSubpackage = "window/multiscreen",
            launch = { mgr -> mgr.activate<WindowMultiScreenToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "mapwindow",
            section = SampleSection.WINDOWS,
            nameResId = R.string.mapwindow_tool_name,
            descResId = R.string.mapwindow_desc,
            apisResId = R.string.mapwindow_apis,
            sourceSubpackage = "window/map",
            launch = { mgr -> mgr.activate<MapWindowToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── MAP ─────────────────────────────────────────────────────────────
        SampleEntry(
            id = "map",
            section = SampleSection.MAP,
            nameResId = R.string.map_tool_name,
            descResId = R.string.map_desc,
            apisResId = R.string.map_apis,
            sourceSubpackage = "map",
            launch = { mgr -> mgr.activate<MapToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "underlay",
            section = SampleSection.MAP,
            nameResId = R.string.underlay_tool_name,
            descResId = R.string.underlay_desc,
            apisResId = R.string.underlay_apis,
            sourceSubpackage = "underlay",
            launch = { mgr -> mgr.activate<UnderlayToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── MAP OVERLAYS ─────────────────────────────────────────────────────
        SampleEntry(
            id = "overlay",
            section = SampleSection.MAP_OVERLAYS,
            nameResId = R.string.overlay_tool_name,
            descResId = R.string.overlay_desc,
            apisResId = R.string.overlay_apis,
            sourceSubpackage = "overlay",
            launch = { mgr -> mgr.activate<OverlaySampleToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "status",
            section = SampleSection.MAP_OVERLAYS,
            nameResId = R.string.status_tool_name,
            descResId = R.string.status_desc,
            apisResId = R.string.status_apis,
            sourceSubpackage = "status",
            launch = { mgr -> mgr.activate<StatusToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "expandable_status",
            section = SampleSection.MAP_OVERLAYS,
            nameResId = R.string.expandable_status_tool_name,
            descResId = R.string.expandable_status_desc,
            apisResId = R.string.expandable_status_apis,
            sourceSubpackage = "expandablestatus",
            launch = { mgr -> mgr.activate<ExpandableStatusToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── HOST UI CHROME ───────────────────────────────────────────────────
        SampleEntry(
            id = "endbar",
            section = SampleSection.MAP_OVERLAYS,
            nameResId = R.string.endbar_tool_name,
            descResId = R.string.endbar_desc,
            apisResId = R.string.endbar_apis,
            sourceSubpackage = "endbar",
            launch = { mgr -> mgr.activate<EndBarSampleToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── UI COMPONENTS ────────────────────────────────────────────────────
        SampleEntry(
            id = "ui_catalog",
            section = SampleSection.UI_COMPONENTS,
            nameResId = R.string.ui_catalog_tool_name,
            descResId = R.string.ui_catalog_desc,
            apisResId = R.string.ui_catalog_apis,
            sourceSubpackage = "uicatalog",
            launch = { mgr -> mgr.activate<UiCatalogToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── HOST UI & DIALOGS ────────────────────────────────────────────────
        SampleEntry(
            id = "dialog",
            section = SampleSection.HOST_UI,
            nameResId = R.string.dialog_tool_name,
            descResId = R.string.dialog_desc,
            apisResId = R.string.dialog_apis,
            sourceSubpackage = "dialog",
            launch = { mgr -> mgr.activate<DialogToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── MODEL & MAP DATA ─────────────────────────────────────────────────
        SampleEntry(
            id = "model",
            section = SampleSection.MODEL_AND_MAP_DATA,
            nameResId = R.string.model_tool_name,
            descResId = R.string.model_desc,
            apisResId = R.string.model_apis,
            sourceSubpackage = "model",
            launch = { mgr -> mgr.activate<ModelToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "map_interactor",
            section = SampleSection.MODEL_AND_MAP_DATA,
            nameResId = R.string.map_interactor_tool_name,
            descResId = R.string.map_interactor_desc,
            apisResId = R.string.map_interactor_apis,
            sourceSubpackage = "mapinteractor",
            launch = { mgr -> mgr.activate<MapInteractorToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── LIFECYCLE ─────────────────────────────────────────────────────────
        SampleEntry(
            id = "service",
            section = SampleSection.LIFECYCLE,
            nameResId = R.string.service_tool_name,
            descResId = R.string.service_desc,
            apisResId = R.string.service_apis,
            sourceSubpackage = "service",
            launch = { mgr -> mgr.activate<ServiceToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),

        // ── RESOURCES & ISOLATION ─────────────────────────────────────────────
        SampleEntry(
            id = "resources_config",
            section = SampleSection.RESOURCES_AND_ISOLATION,
            nameResId = R.string.config_tool_name,
            descResId = R.string.config_desc,
            apisResId = R.string.config_apis,
            sourceSubpackage = "resources/config",
            launch = { mgr -> mgr.activate<ConfigToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "resources_material",
            section = SampleSection.RESOURCES_AND_ISOLATION,
            nameResId = R.string.material_tool_name,
            descResId = R.string.material_desc,
            apisResId = R.string.material_apis,
            sourceSubpackage = "resources/material",
            launch = { mgr -> mgr.activate<MaterialToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "resources_collision",
            section = SampleSection.RESOURCES_AND_ISOLATION,
            nameResId = R.string.collision_tool_name,
            descResId = R.string.collision_desc,
            apisResId = R.string.collision_apis,
            sourceSubpackage = "resources/collision",
            launch = { mgr -> mgr.activate<CollisionToolDescriptor>(flags = ToolManager.FLAG_COMPONENT_ON_TOP) },
        ),
        SampleEntry(
            id = "native_cross_apk",
            section = SampleSection.RESOURCES_AND_ISOLATION,
            nameResId = R.string.native_cross_apk_title,
            descResId = R.string.native_cross_apk_desc,
            apisResId = R.string.native_cross_apk_apis,
            sourceSubpackage = "isolation/nativelib",
            isCrossApk = true,
            crossApkFqcn = ISOLATION_NATIVE_FQCN,
            launch = { mgr ->
                mgr.resolveToolId(ISOLATION_NATIVE_FQCN)
                    ?.let { mgr.activate(it, ToolManager.FLAG_COMPONENT_ON_TOP) }
            },
        ),
    )
}

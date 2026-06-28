package vision.combat.c4.ds.sample.gallery.catalog.ui

import androidx.annotation.Keep
import androidx.annotation.StringRes
import vision.combat.c4.ds.sample.gallery.R
import vision.combat.c4.ds.sample.gallery.dialog.DialogToolDescriptor
import vision.combat.c4.ds.sample.gallery.endbar.EndBarSampleToolDescriptor
import vision.combat.c4.ds.sample.gallery.expandablestatus.ExpandableStatusToolDescriptor
import vision.combat.c4.ds.sample.gallery.map.MapToolDescriptor
import vision.combat.c4.ds.sample.gallery.mapinteractor.MapInteractorToolDescriptor
import vision.combat.c4.ds.sample.gallery.model.ModelToolDescriptor
import vision.combat.c4.ds.sample.gallery.overlay.OverlaySampleToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.collision.CollisionToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.config.ConfigToolDescriptor
import vision.combat.c4.ds.sample.gallery.resources.material.MaterialToolDescriptor
import vision.combat.c4.ds.sample.gallery.service.ServiceToolDescriptor
import vision.combat.c4.ds.sample.gallery.status.StatusToolDescriptor
import vision.combat.c4.ds.sample.gallery.uicatalog.UiCatalogToolDescriptor
import vision.combat.c4.ds.sample.gallery.underlay.UnderlayToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.map.MapWindowToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.multiscreen.WindowMultiScreenToolDescriptor
import vision.combat.c4.ds.sample.gallery.window.singlescreen.WindowSingleScreenToolDescriptor
import vision.combat.c4.ds.sdk.tool.requireQualifiedName

/**
 * Registry of all gallery samples.
 *
 * @param section Which section this entry belongs to.
 * @param nameResId String resource for the sample name.
 * @param descResId String resource for the sample description.
 * @param apisResId String resource listing which SDK APIs are demonstrated.
 * @param sourceSubpackage Package path shown on the detail screen (e.g. "window/singlescreen").
 * @param toolClassName Fully-qualified class name of the tool's descriptor. Used both to match this
 *   entry against the active tools (ToolManager.activeTools) and as the argument to
 *   ToolManager.activate / deactivate, which resolve it to a ToolId via resolveToolId.
 * @param isCrossApk True if this entry's tool lives in a separate APK that must be installed.
 */
@Keep
internal enum class CatalogEntry(
    val section: CatalogSection,
    @get:StringRes val nameResId: Int,
    @get:StringRes val descResId: Int,
    @get:StringRes val apisResId: Int,
    val sourceSubpackage: String,
    val toolClassName: String,
    val isCrossApk: Boolean = false,
) {
    // ── WINDOWS ─────────────────────────────────────────────────────────
    WINDOW_SINGLE_SCREEN(
        section = CatalogSection.WINDOWS,
        nameResId = R.string.window_single_screen_tool_name,
        descResId = R.string.window_single_screen_desc,
        apisResId = R.string.window_single_screen_apis,
        sourceSubpackage = "window/singlescreen",
        toolClassName = requireQualifiedName<WindowSingleScreenToolDescriptor>(),
    ),
    WINDOW_MULTI_SCREEN(
        section = CatalogSection.WINDOWS,
        nameResId = R.string.window_multi_screen_tool_name,
        descResId = R.string.window_multi_screen_desc,
        apisResId = R.string.window_multi_screen_apis,
        sourceSubpackage = "window/multiscreen",
        toolClassName = requireQualifiedName<WindowMultiScreenToolDescriptor>(),
    ),
    MAP_WINDOW(
        section = CatalogSection.WINDOWS,
        nameResId = R.string.mapwindow_tool_name,
        descResId = R.string.mapwindow_desc,
        apisResId = R.string.mapwindow_apis,
        sourceSubpackage = "window/map",
        toolClassName = requireQualifiedName<MapWindowToolDescriptor>(),
    ),

    // ── MAP ─────────────────────────────────────────────────────────────
    MAP(
        section = CatalogSection.MAP,
        nameResId = R.string.map_tool_name,
        descResId = R.string.map_desc,
        apisResId = R.string.map_apis,
        sourceSubpackage = "map",
        toolClassName = requireQualifiedName<MapToolDescriptor>(),
    ),
    UNDERLAY(
        section = CatalogSection.MAP,
        nameResId = R.string.underlay_tool_name,
        descResId = R.string.underlay_desc,
        apisResId = R.string.underlay_apis,
        sourceSubpackage = "underlay",
        toolClassName = requireQualifiedName<UnderlayToolDescriptor>(),
    ),

    // ── MAP OVERLAYS ─────────────────────────────────────────────────────
    OVERLAY(
        section = CatalogSection.MAP_OVERLAYS,
        nameResId = R.string.overlay_tool_name,
        descResId = R.string.overlay_desc,
        apisResId = R.string.overlay_apis,
        sourceSubpackage = "overlay",
        toolClassName = requireQualifiedName<OverlaySampleToolDescriptor>(),
    ),
    STATUS(
        section = CatalogSection.MAP_OVERLAYS,
        nameResId = R.string.status_tool_name,
        descResId = R.string.status_desc,
        apisResId = R.string.status_apis,
        sourceSubpackage = "status",
        toolClassName = requireQualifiedName<StatusToolDescriptor>(),
    ),
    EXPANDABLE_STATUS(
        section = CatalogSection.MAP_OVERLAYS,
        nameResId = R.string.expandable_status_tool_name,
        descResId = R.string.expandable_status_desc,
        apisResId = R.string.expandable_status_apis,
        sourceSubpackage = "expandablestatus",
        toolClassName = requireQualifiedName<ExpandableStatusToolDescriptor>(),
    ),

    // ── HOST UI CHROME ───────────────────────────────────────────────────
    ENDBAR(
        section = CatalogSection.MAP_OVERLAYS,
        nameResId = R.string.endbar_tool_name,
        descResId = R.string.endbar_desc,
        apisResId = R.string.endbar_apis,
        sourceSubpackage = "endbar",
        toolClassName = requireQualifiedName<EndBarSampleToolDescriptor>(),
    ),

    // ── UI COMPONENTS ────────────────────────────────────────────────────
    UI_CATALOG(
        section = CatalogSection.UI_COMPONENTS,
        nameResId = R.string.ui_catalog_tool_name,
        descResId = R.string.ui_catalog_desc,
        apisResId = R.string.ui_catalog_apis,
        sourceSubpackage = "uicatalog",
        toolClassName = requireQualifiedName<UiCatalogToolDescriptor>(),
    ),

    // ── HOST UI & DIALOGS ────────────────────────────────────────────────
    DIALOG(
        section = CatalogSection.HOST_UI,
        nameResId = R.string.dialog_tool_name,
        descResId = R.string.dialog_desc,
        apisResId = R.string.dialog_apis,
        sourceSubpackage = "dialog",
        toolClassName = requireQualifiedName<DialogToolDescriptor>(),
    ),

    // ── MODEL & MAP DATA ─────────────────────────────────────────────────
    MODEL(
        section = CatalogSection.MODEL_AND_MAP_DATA,
        nameResId = R.string.model_tool_name,
        descResId = R.string.model_desc,
        apisResId = R.string.model_apis,
        sourceSubpackage = "model",
        toolClassName = requireQualifiedName<ModelToolDescriptor>(),
    ),
    MAP_INTERACTOR(
        section = CatalogSection.MODEL_AND_MAP_DATA,
        nameResId = R.string.map_interactor_tool_name,
        descResId = R.string.map_interactor_desc,
        apisResId = R.string.map_interactor_apis,
        sourceSubpackage = "mapinteractor",
        toolClassName = requireQualifiedName<MapInteractorToolDescriptor>(),
    ),

    // ── LIFECYCLE ─────────────────────────────────────────────────────────
    SERVICE(
        section = CatalogSection.LIFECYCLE,
        nameResId = R.string.service_tool_name,
        descResId = R.string.service_desc,
        apisResId = R.string.service_apis,
        sourceSubpackage = "service",
        toolClassName = requireQualifiedName<ServiceToolDescriptor>(),
    ),

    // ── RESOURCES & ISOLATION ─────────────────────────────────────────────
    RESOURCES_CONFIG(
        section = CatalogSection.RESOURCES_AND_ISOLATION,
        nameResId = R.string.config_tool_name,
        descResId = R.string.config_desc,
        apisResId = R.string.config_apis,
        sourceSubpackage = "resources/config",
        toolClassName = requireQualifiedName<ConfigToolDescriptor>(),
    ),
    RESOURCES_MATERIAL(
        section = CatalogSection.RESOURCES_AND_ISOLATION,
        nameResId = R.string.material_tool_name,
        descResId = R.string.material_desc,
        apisResId = R.string.material_apis,
        sourceSubpackage = "resources/material",
        toolClassName = requireQualifiedName<MaterialToolDescriptor>(),
    ),
    RESOURCES_COLLISION(
        section = CatalogSection.RESOURCES_AND_ISOLATION,
        nameResId = R.string.collision_tool_name,
        descResId = R.string.collision_desc,
        apisResId = R.string.collision_apis,
        sourceSubpackage = "resources/collision",
        toolClassName = requireQualifiedName<CollisionToolDescriptor>(),
    ),
    NATIVE_CROSS_APK(
        section = CatalogSection.RESOURCES_AND_ISOLATION,
        nameResId = R.string.native_cross_apk_title,
        descResId = R.string.native_cross_apk_desc,
        apisResId = R.string.native_cross_apk_apis,
        sourceSubpackage = "isolation/nativelib",
        toolClassName = "vision.combat.c4.ds.sample.isolation.nativelib.NativeToolDescriptor",
        isCrossApk = true,
    )
}

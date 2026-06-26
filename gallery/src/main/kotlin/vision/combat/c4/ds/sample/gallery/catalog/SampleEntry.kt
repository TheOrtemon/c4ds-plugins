package vision.combat.c4.ds.sample.gallery.catalog

import androidx.annotation.StringRes
import vision.combat.c4.ds.sdk.tool.ToolManager

/**
 * A single sample entry in the catalog.
 *
 * @param id Stable string identifier for navigation (e.g. "window_single_screen").
 * @param section Which section this entry belongs to.
 * @param nameResId String resource for the sample name.
 * @param descResId String resource for the sample description.
 * @param apisResId String resource listing which SDK APIs are demonstrated.
 * @param sourceSubpackage Package path shown on the detail screen (e.g. "window/singlescreen").
 * @param isCrossApk True if this entry requires a separate APK to be installed.
 * @param crossApkFqcn If [isCrossApk], the FQCN of the target descriptor for resolveToolId.
 * @param launch Called when the user taps the list row to activate the sample. May be non-null for
 *   cross-APK entries that use [ToolManager.resolveToolId] before [ToolManager.activate].
 */
data class SampleEntry(
    val id: String,
    val section: SampleSection,
    @StringRes val nameResId: Int,
    @StringRes val descResId: Int,
    @StringRes val apisResId: Int,
    val sourceSubpackage: String,
    val isCrossApk: Boolean = false,
    val crossApkFqcn: String? = null,
    val launch: ((ToolManager) -> Unit)? = null,
)


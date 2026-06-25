package vision.combat.c4.ds.sample.gallery.catalog

import androidx.annotation.StringRes
import vision.combat.c4.ds.sdk.tool.ToolManager

/**
 * A single sample entry in the catalog.
 *
 * @param id Stable string identifier for navigation (e.g. "window_simple").
 * @param section Which section this entry belongs to.
 * @param nameResId String resource for the sample name.
 * @param descResId String resource for the sample description.
 * @param apisResId String resource listing which SDK APIs are demonstrated.
 * @param sourceSubpackage Package path shown on the detail screen (e.g. "window/simple").
 * @param isCrossApk True if this entry requires a separate APK to be installed.
 * @param crossApkFqcn If [isCrossApk], the FQCN of the target descriptor for resolveToolId.
 * @param launch Called when the user taps Launch. Null for cross-APK entries handled separately.
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


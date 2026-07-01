package vision.combat.c4.ds.sample.gallery.renderable.ui

import android.graphics.Color

/**
 * A small set of colors the user can pick from the Renderables controls panel.
 * The [androidColor] value is passed directly to WorldWind color constructors.
 */
enum class RenderableColor(val androidColor: Int) {
    CYAN(Color.CYAN),
    RED(Color.RED),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    WHITE(Color.WHITE),
}

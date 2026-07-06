package vision.combat.c4.ds.sample.bookmarks.domain.model

/**
 * Plain domain model for a single saved bookmark. Public because it crosses the
 * `:bookmarks:data` / `:bookmarks:app` module boundary.
 */
public data class Bookmark(
    val id: String,
    val label: String,
    val target: String,
)

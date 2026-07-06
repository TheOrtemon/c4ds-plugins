package vision.combat.c4.ds.sample.bookmarks.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Regression coverage for the "added bookmark never appears in the list" bug: bookmarks used
 * to be persisted as a `Set<String>` observed through the SDK's generic `observeAsStateFlow`,
 * which never emitted updates for `add`. [BookmarkRepositoryImpl] now stores the whole list as
 * one JSON-array `String` and observes it via the proven `String` path.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class BookmarkRepositoryImplTest {

    @Test
    fun `add makes the bookmark visible on the observed state flow`() = runTest {
        val preferences = FakeSharedPreferences()
        val repository = BookmarkRepositoryImpl(preferences)

        val state = repository.observeBookmarks(backgroundScope)
        runCurrent()

        repository.add(label = "Home", target = "vision.combat.c4.ds.sample.bookmarks/home")
        runCurrent()

        val bookmarks = state.value
        assertEquals(1, bookmarks.size)
        assertEquals("Home", bookmarks.single().label)
        assertEquals("vision.combat.c4.ds.sample.bookmarks/home", bookmarks.single().target)
    }

    @Test
    fun `bookmark with a pipe character in the target still appears`() = runTest {
        val preferences = FakeSharedPreferences()
        val repository = BookmarkRepositoryImpl(preferences)

        val state = repository.observeBookmarks(backgroundScope)
        runCurrent()

        repository.add(label = "Pipe target", target = "a|b|c")
        runCurrent()

        val bookmarks = state.value
        assertTrue(bookmarks.any { it.label == "Pipe target" && it.target == "a|b|c" })
    }

    @Test
    fun `clear empties the list`() = runTest {
        val preferences = FakeSharedPreferences()
        val repository = BookmarkRepositoryImpl(preferences)

        val state = repository.observeBookmarks(backgroundScope)
        runCurrent()

        repository.add(label = "Home", target = "home")
        runCurrent()
        assertEquals(1, state.value.size)

        repository.clear()
        runCurrent()

        assertTrue(state.value.isEmpty())
    }
}

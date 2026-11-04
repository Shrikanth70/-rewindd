package com.rewindd.app.feature.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SaveResult
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.SavedItem
import com.rewindd.app.domain.model.Tag
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class LibraryTab {
    ALL,
    INBOX,
    ARCHIVED,
    PINNED
}

enum class LibrarySortMode(val label: String) {
    RECENT("Recent"),
    OLDEST("Oldest"),
    TITLE_AZ("Title (A-Z)")
}

data class LibraryCounts(
    val total: Int = 0,
    val all: Int = 0,
    val inbox: Int = 0,
    val archived: Int = 0,
    val pinned: Int = 0,
    val activeFiltersCount: Int = 0
)

sealed class SaveFeedbackEvent {
    data class Saved(val item: SavedItem) : SaveFeedbackEvent()
    data class DuplicateDetected(val existingItem: SavedItem) : SaveFeedbackEvent()
    data class InvalidUrl(val reason: String) : SaveFeedbackEvent()
    data class Error(val message: String) : SaveFeedbackEvent()
}

class LibraryViewModel(
    private val savedItemRepository: SavedItemRepository,
    private val collectionRepository: CollectionRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _activeTab = MutableStateFlow(LibraryTab.ALL)
    val activeTab: StateFlow<LibraryTab> = _activeTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCollectionId = MutableStateFlow<String?>(null)
    val selectedCollectionId: StateFlow<String?> = _selectedCollectionId.asStateFlow()

    private val _selectedTagId = MutableStateFlow<String?>(null)
    val selectedTagId: StateFlow<String?> = _selectedTagId.asStateFlow()

    private val _sortMode = MutableStateFlow(LibrarySortMode.RECENT)
    val sortMode: StateFlow<LibrarySortMode> = _sortMode.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _feedbackEvents = MutableSharedFlow<SaveFeedbackEvent>()
    val feedbackEvents: SharedFlow<SaveFeedbackEvent> = _feedbackEvents.asSharedFlow()

    val allCollections: StateFlow<List<Collection>> = collectionRepository.getAllCollections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags: StateFlow<List<Tag>> = tagRepository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rawItemsWithDetails: StateFlow<List<SavedItemDetailsDomain>> = savedItemRepository.getAllWithDetails()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Backward-compatible for any old callers
    val itemsState: StateFlow<List<SavedItem>> = savedItemRepository.getAllItems()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val counts: StateFlow<LibraryCounts> = combine(
        rawItemsWithDetails,
        _selectedCollectionId,
        _selectedTagId
    ) { items, collId, tagId ->
        var active = 0
        if (collId != null) active++
        if (tagId != null) active++
        LibraryCounts(
            total = items.size,
            all = items.count { it.item.reviewState != ReviewState.ARCHIVED },
            inbox = items.count { it.item.reviewState == ReviewState.UNREVIEWED },
            archived = items.count { it.item.reviewState == ReviewState.ARCHIVED },
            pinned = items.count { it.item.isPinned && it.item.reviewState != ReviewState.ARCHIVED },
            activeFiltersCount = active
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, LibraryCounts())

    val collectionItemCounts: StateFlow<Map<String, Int>> = combine(
        rawItemsWithDetails,
        allCollections
    ) { items, collections ->
        collections.associate { col ->
            col.id to items.count { it.item.collectionId == col.id }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    private val rawFilteredItems = combine(
        rawItemsWithDetails,
        _activeTab,
        _searchQuery,
        _selectedCollectionId,
        _selectedTagId
    ) { items, tab, query, collectionId, tagId ->
        items.filter { itemDetails ->
            val item = itemDetails.item

            // 1. Tab filter
            val matchesTab = when (tab) {
                LibraryTab.ALL -> item.reviewState != ReviewState.ARCHIVED
                LibraryTab.INBOX -> item.reviewState == ReviewState.UNREVIEWED
                LibraryTab.ARCHIVED -> item.reviewState == ReviewState.ARCHIVED
                LibraryTab.PINNED -> item.isPinned && item.reviewState != ReviewState.ARCHIVED
            }
            if (!matchesTab) return@filter false

            // 2. Collection filter
            if (collectionId != null && item.collectionId != collectionId) {
                return@filter false
            }

            // 3. Tag filter
            if (tagId != null && itemDetails.tags.none { it.id == tagId }) {
                return@filter false
            }

            // 4. Search query
            if (query.isNotBlank()) {
                val q = query.trim().lowercase()
                val matchesTitle = item.title.lowercase().contains(q)
                val matchesUrl = item.normalizedUrl.lowercase().contains(q)
                val matchesNote = item.note?.lowercase()?.contains(q) == true
                val matchesTag = itemDetails.tags.any { it.normalizedName.contains(q) || it.displayName.lowercase().contains(q) }
                if (!matchesTitle && !matchesUrl && !matchesNote && !matchesTag) {
                    return@filter false
                }
            }

            true
        }
    }

    val filteredItems: StateFlow<List<SavedItemDetailsDomain>> = rawFilteredItems.combine(_sortMode) { filtered, sort ->
        when (sort) {
            LibrarySortMode.RECENT -> filtered.sortedByDescending { it.item.capturedAtUtc }
            LibrarySortMode.OLDEST -> filtered.sortedBy { it.item.capturedAtUtc }
            LibrarySortMode.TITLE_AZ -> filtered.sortedBy { it.item.title.lowercase() }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setTab(tab: LibraryTab) {
        _activeTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortMode(mode: LibrarySortMode) {
        _sortMode.value = mode
    }

    fun selectCollection(collectionId: String?) {
        _selectedCollectionId.value = collectionId
    }

    fun selectTag(tagId: String?) {
        _selectedTagId.value = tagId
    }

    fun clearFilters() {
        _selectedCollectionId.value = null
        _selectedTagId.value = null
        _searchQuery.value = ""
    }

    fun saveUrl(
        rawInput: String,
        sourcePackage: String? = null,
        sourceLabel: String? = null,
        collectionId: String? = null,
        note: String? = null,
        onSuccess: ((SavedItem) -> Unit)? = null
    ) {
        viewModelScope.launch {
            _isSaving.value = true
            when (val result = savedItemRepository.saveUrl(rawInput, sourcePackage, sourceLabel)) {
                is SaveResult.Success -> {
                    var item = result.item
                    if (collectionId != null || !note.isNullOrBlank()) {
                        savedItemRepository.updateDetails(
                            id = item.id,
                            title = item.title,
                            note = note,
                            collectionId = collectionId,
                            isPinned = item.isPinned
                        )
                        item = savedItemRepository.getById(item.id) ?: item
                    }
                    _feedbackEvents.emit(SaveFeedbackEvent.Saved(item))
                    onSuccess?.invoke(item)
                }
                is SaveResult.Duplicate -> {
                    _feedbackEvents.emit(SaveFeedbackEvent.DuplicateDetected(result.existingItem))
                }
                is SaveResult.Invalid -> {
                    _feedbackEvents.emit(SaveFeedbackEvent.InvalidUrl(result.reason))
                }
                is SaveResult.Failure -> {
                    _feedbackEvents.emit(SaveFeedbackEvent.Error(result.error))
                }
            }
            _isSaving.value = false
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            savedItemRepository.deleteById(id)
        }
    }

    fun togglePin(id: String) {
        viewModelScope.launch {
            savedItemRepository.togglePin(id)
        }
    }

    fun updateReviewState(id: String, state: ReviewState) {
        viewModelScope.launch {
            savedItemRepository.updateReviewState(id, state)
        }
    }

    fun createCollection(
        name: String,
        colorToken: String,
        isPinned: Boolean = false,
        iconToken: String = "📁",
        onCreated: ((Collection) -> Unit)? = null
    ) {
        viewModelScope.launch {
            val created = collectionRepository.createCollection(
                name = name,
                colorToken = colorToken,
                isPinned = isPinned,
                iconToken = iconToken
            )
            onCreated?.invoke(created)
        }
    }

    fun updateCollection(collection: Collection) {
        viewModelScope.launch {
            collectionRepository.updateCollection(collection)
        }
    }

    fun toggleCollectionPin(collection: Collection) {
        viewModelScope.launch {
            collectionRepository.updateCollection(collection.copy(isPinned = !collection.isPinned))
        }
    }

    fun assignItemToCollection(itemId: String, collectionId: String?) {
        viewModelScope.launch {
            savedItemRepository.assignItemToCollection(itemId, collectionId)
        }
    }

    fun removeItemFromCollection(itemId: String) {
        viewModelScope.launch {
            savedItemRepository.removeItemFromCollection(itemId)
        }
    }

    fun deleteCollection(id: String) {
        viewModelScope.launch {
            if (_selectedCollectionId.value == id) {
                _selectedCollectionId.value = null
            }
            collectionRepository.deleteCollection(id)
        }
    }

    fun createTag(name: String, colorToken: String? = null, onCreated: ((Tag) -> Unit)? = null) {
        viewModelScope.launch {
            val tag = tagRepository.createOrFindTag(name, colorToken)
            onCreated?.invoke(tag)
        }
    }

    fun deleteTag(id: String) {
        viewModelScope.launch {
            if (_selectedTagId.value == id) {
                _selectedTagId.value = null
            }
            tagRepository.deleteTag(id)
        }
    }
}

package com.rewindd.app.feature.itemdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rewindd.app.core.database.model.SavedItemDetailsDomain
import com.rewindd.app.data.repository.CollectionRepository
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.data.repository.TagRepository
import com.rewindd.app.domain.model.Collection
import com.rewindd.app.domain.model.ReviewState
import com.rewindd.app.domain.model.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ItemDetailViewModel(
    private val itemId: String,
    private val savedItemRepository: SavedItemRepository,
    private val collectionRepository: CollectionRepository,
    private val tagRepository: TagRepository
) : ViewModel() {

    private val _itemDetails = MutableStateFlow<SavedItemDetailsDomain?>(null)
    val itemDetails: StateFlow<SavedItemDetailsDomain?> = _itemDetails.asStateFlow()

    val allCollections: StateFlow<List<Collection>> = collectionRepository.getAllCollections()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTags: StateFlow<List<Tag>> = tagRepository.getAllTags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadItem()
    }

    fun loadItem() {
        viewModelScope.launch {
            _itemDetails.value = savedItemRepository.getWithDetailsById(itemId)
        }
    }

    fun togglePin() {
        viewModelScope.launch {
            savedItemRepository.togglePin(itemId)
            loadItem()
        }
    }

    fun keepItem() {
        viewModelScope.launch {
            savedItemRepository.keepItem(itemId)
            loadItem()
        }
    }

    fun archiveItem() {
        viewModelScope.launch {
            savedItemRepository.archiveItem(itemId)
            loadItem()
        }
    }

    fun deleteItem(onDeleted: () -> Unit) {
        viewModelScope.launch {
            savedItemRepository.deleteById(itemId)
            onDeleted()
        }
    }

    fun saveEdits(
        title: String,
        note: String?,
        collectionId: String?,
        isPinned: Boolean,
        tagIds: List<String>,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch {
            savedItemRepository.updateDetails(
                id = itemId,
                title = title,
                note = note,
                collectionId = collectionId,
                isPinned = isPinned
            )
            tagRepository.setTagsForItem(itemId, tagIds)
            loadItem()
            onSaved()
        }
    }

    fun createAndAddTag(displayName: String, onTagCreated: (Tag) -> Unit) {
        viewModelScope.launch {
            val tag = tagRepository.createOrFindTag(displayName)
            onTagCreated(tag)
        }
    }

    fun applyTag(displayName: String) {
        viewModelScope.launch {
            val tag = tagRepository.createOrFindTag(displayName)
            val currentTags = _itemDetails.value?.tags?.map { it.id }?.toMutableList() ?: mutableListOf()
            if (!currentTags.contains(tag.id)) {
                currentTags.add(tag.id)
                tagRepository.setTagsForItem(itemId, currentTags)
                loadItem()
            }
        }
    }
}

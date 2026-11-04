package com.rewindd.app.feature.rewind

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.domain.model.SavedItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class RewindDeckState {
    object Loading : RewindDeckState()
    object IntroGuide : RewindDeckState()
    data class ActiveDeck(
        val currentItem: SavedItem,
        val currentIndex: Int,
        val totalInSession: Int
    ) : RewindDeckState()
    data class EmptyQueue(
        val totalReviewedToday: Int = 0
    ) : RewindDeckState()
}

class RewindViewModel(
    private val savedItemRepository: SavedItemRepository,
    private val undoTimeoutMs: Long = 5_000L
) : ViewModel() {

    private val _deckState = MutableStateFlow<RewindDeckState>(RewindDeckState.Loading)
    val deckState: StateFlow<RewindDeckState> = _deckState.asStateFlow()

    private val _undoBannerItem = MutableStateFlow<SavedItem?>(null)
    val undoBannerItem: StateFlow<SavedItem?> = _undoBannerItem.asStateFlow()

    private var unreviewedQueue: List<SavedItem> = emptyList()
    private var currentQueueIndex = 0
    private var sessionReviewedCount = 0
    private var initialSessionTotal = 0
    private var undoJob: Job? = null

    val unreviewedCount: StateFlow<Int> = savedItemRepository.getUnreviewedCount()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    init {
        observeQueue()
    }

    private fun observeQueue() {
        viewModelScope.launch {
            savedItemRepository.getUnreviewedItems().collect { queue ->
                unreviewedQueue = queue
                if (initialSessionTotal == 0 || queue.size > initialSessionTotal) {
                    initialSessionTotal = queue.size
                }

                if (queue.isEmpty()) {
                    currentQueueIndex = 0
                    _deckState.value = RewindDeckState.EmptyQueue(totalReviewedToday = sessionReviewedCount)
                } else {
                    currentQueueIndex = currentQueueIndex.coerceIn(0, queue.size - 1)
                    updateActiveDeck()
                }
            }
        }
    }

    private fun updateActiveDeck() {
        if (unreviewedQueue.isEmpty()) return
        currentQueueIndex = currentQueueIndex.coerceIn(0, unreviewedQueue.size - 1)
        val current = unreviewedQueue[currentQueueIndex]
        _deckState.value = RewindDeckState.ActiveDeck(
            currentItem = current,
            currentIndex = currentQueueIndex + 1,
            totalInSession = unreviewedQueue.size
        )
    }

    fun nextItem() {
        if (currentQueueIndex < unreviewedQueue.size - 1) {
            currentQueueIndex++
            updateActiveDeck()
        }
    }

    fun previousItem() {
        if (currentQueueIndex > 0) {
            currentQueueIndex--
            updateActiveDeck()
        }
    }

    private var lastArchivedItem: SavedItem? = null

    fun keepCurrentItem() {
        val current = unreviewedQueue.getOrNull(currentQueueIndex) ?: return
        viewModelScope.launch {
            savedItemRepository.keepItem(current.id)
            sessionReviewedCount++
            clearUndo()
        }
    }

    fun archiveCurrentItem() {
        val current = unreviewedQueue.getOrNull(currentQueueIndex) ?: return
        lastArchivedItem = current
        viewModelScope.launch {
            savedItemRepository.archiveItem(current.id)
            sessionReviewedCount++
            showUndoBanner(current)
        }
    }

    private fun showUndoBanner(item: SavedItem) {
        undoJob?.cancel()
        _undoBannerItem.value = item
        if (undoTimeoutMs > 0) {
            undoJob = viewModelScope.launch {
                delay(undoTimeoutMs)
                _undoBannerItem.value = null
                lastArchivedItem = null
            }
        }
    }

    fun undoArchive() {
        val item = _undoBannerItem.value ?: lastArchivedItem ?: return
        clearUndo()
        viewModelScope.launch {
            savedItemRepository.undoArchive(item.id)
            if (sessionReviewedCount > 0) sessionReviewedCount--
        }
    }

    fun clearUndo() {
        undoJob?.cancel()
        _undoBannerItem.value = null
        lastArchivedItem = null
    }

    fun skipIntro() {
        if (unreviewedQueue.isEmpty()) {
            _deckState.value = RewindDeckState.EmptyQueue(totalReviewedToday = sessionReviewedCount)
        } else {
            currentQueueIndex = 0
            updateActiveDeck()
        }
    }
}

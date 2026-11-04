package com.rewindd.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rewindd.app.data.repository.SavedItemRepository
import com.rewindd.app.domain.model.SavedItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val savedItemRepository: SavedItemRepository
) : ViewModel() {

    val unreviewedCount: StateFlow<Int> = savedItemRepository.getUnreviewedCount()
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val topUnreviewedItem: StateFlow<SavedItem?> = savedItemRepository.getUnreviewedItems()
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

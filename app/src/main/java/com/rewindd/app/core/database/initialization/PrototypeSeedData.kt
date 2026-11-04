package com.rewindd.app.core.database.initialization

import com.rewindd.app.core.database.entity.SavedItemEntity

object PrototypeSeedData {

    /**
     * In production, the vault starts completely clean for genuine user-initiated capture.
     * Returns an empty list so zero dummy/prototype links are pre-populated.
     */
    fun getInitialItems(): List<SavedItemEntity> {
        return emptyList()
    }
}

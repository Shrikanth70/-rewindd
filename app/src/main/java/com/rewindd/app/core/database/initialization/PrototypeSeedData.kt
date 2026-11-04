package com.rewindd.app.core.database.initialization

import com.rewindd.app.core.database.entity.SavedItemEntity
import java.util.UUID

object PrototypeSeedData {

    fun getInitialItems(): List<SavedItemEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            SavedItemEntity(
                id = "item-substack-ai-agent",
                url = "https://danluu.com/agent-loops",
                normalizedUrl = "https://danluu.com/agent-loops",
                sourcePackage = "com.android.chrome",
                sourceLabel = "SUBSTACK.COM",
                title = "The architecture behind a useful AI agent",
                thumbnailUri = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800&auto=format&fit=crop&q=80",
                capturedAtUtc = now - (2 * 86400000L),
                updatedAtUtc = now - (2 * 86400000L),
                reviewState = "UNREVIEWED",
                isPinned = true,
                collectionId = null,
                note = "Key reference for architectural loop handling in week 14 sprint. Need to check deterministic state trees.",
                metadataStatus = "SUCCESS",
                archivedAtUtc = null,
                contentHash = null
            ),
            SavedItemEntity(
                id = "item-arxiv-rag-eval",
                url = "https://arxiv.org/abs/2402.148",
                normalizedUrl = "https://arxiv.org/abs/2402.148",
                sourcePackage = "com.android.chrome",
                sourceLabel = "ARXIV / DEEPLEARNING",
                title = "A practical guide to RAG evaluation",
                thumbnailUri = "https://images.unsplash.com/photo-1620641788421-7a1c342ea42e?w=800&auto=format&fit=crop&q=80",
                capturedAtUtc = now - (4 * 86400000L),
                updatedAtUtc = now - (4 * 86400000L),
                reviewState = "UNREVIEWED",
                isPinned = false,
                collectionId = null,
                note = "Check faithfulness score > 0.88 metric against our local prompt evals.",
                metadataStatus = "SUCCESS",
                archivedAtUtc = null,
                contentHash = null
            ),
            SavedItemEntity(
                id = "item-instagram-habits-reel",
                url = "https://www.instagram.com/reel/C8xyz123456/",
                normalizedUrl = "https://www.instagram.com/reel/c8xyz123456",
                sourcePackage = "com.instagram.android",
                sourceLabel = "INSTAGRAM REEL",
                title = "5 Mindful Screen Habits for Tech Workers",
                thumbnailUri = "https://images.unsplash.com/photo-1512486130939-2c4f79935e4f?w=800&auto=format&fit=crop&q=80",
                capturedAtUtc = now - (1 * 86400000L),
                updatedAtUtc = now - (1 * 86400000L),
                reviewState = "UNREVIEWED",
                isPinned = false,
                collectionId = null,
                note = "Try the 20-minute boundary rule during deep work blocks.",
                metadataStatus = "SUCCESS",
                archivedAtUtc = null,
                contentHash = null
            ),
            SavedItemEntity(
                id = "item-youtube-autonomous-agents",
                url = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                normalizedUrl = "https://www.youtube.com/watch?v=dqw4w9wgxcq",
                sourcePackage = "com.google.android.youtube",
                sourceLabel = "YOUTUBE",
                title = "Building Autonomous Agents from Scratch",
                thumbnailUri = "https://img.youtube.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                capturedAtUtc = now - (3 * 86400000L),
                updatedAtUtc = now - (3 * 86400000L),
                reviewState = "UNREVIEWED",
                isPinned = false,
                collectionId = null,
                note = "Watch section on vector search index updates.",
                metadataStatus = "SUCCESS",
                archivedAtUtc = null,
                contentHash = null
            )
        )
    }
}

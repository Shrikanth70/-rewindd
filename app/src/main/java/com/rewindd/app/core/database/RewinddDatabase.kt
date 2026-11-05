package com.rewindd.app.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rewindd.app.core.database.dao.CollectionDao
import com.rewindd.app.core.database.dao.DailyAppUsageDao
import com.rewindd.app.core.database.dao.SavedItemDao
import com.rewindd.app.core.database.dao.TagDao
import com.rewindd.app.core.database.dao.UserPreferencesDao
import com.rewindd.app.core.database.entity.CollectionEntity
import com.rewindd.app.core.database.entity.DailyAppUsageEntity
import com.rewindd.app.core.database.entity.SavedItemEntity
import com.rewindd.app.core.database.entity.SavedItemTagEntity
import com.rewindd.app.core.database.entity.TagEntity
import com.rewindd.app.core.database.entity.UserPreferencesEntity

@Database(
    entities = [
        SavedItemEntity::class,
        CollectionEntity::class,
        TagEntity::class,
        SavedItemTagEntity::class,
        DailyAppUsageEntity::class,
        UserPreferencesEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class RewinddDatabase : RoomDatabase() {

    abstract fun savedItemDao(): SavedItemDao
    abstract fun collectionDao(): CollectionDao
    abstract fun tagDao(): TagDao
    abstract fun dailyAppUsageDao(): DailyAppUsageDao
    abstract fun userPreferencesDao(): UserPreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: RewinddDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `collections` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `name` TEXT NOT NULL,
                        `colorToken` TEXT NOT NULL,
                        `createdAtUtc` INTEGER NOT NULL,
                        `updatedAtUtc` INTEGER NOT NULL,
                        `isPinned` INTEGER NOT NULL DEFAULT 0,
                        `sortOrder` INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `tags` (
                        `id` TEXT NOT NULL PRIMARY KEY,
                        `normalizedName` TEXT NOT NULL,
                        `displayName` TEXT NOT NULL,
                        `colorToken` TEXT
                    )
                """.trimIndent())
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tags_normalizedName` ON `tags` (`normalizedName`)")

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `saved_item_tags` (
                        `savedItemId` TEXT NOT NULL,
                        `tagId` TEXT NOT NULL,
                        PRIMARY KEY(`savedItemId`, `tagId`),
                        FOREIGN KEY(`savedItemId`) REFERENCES `saved_items`(`id`) ON DELETE CASCADE,
                        FOREIGN KEY(`tagId`) REFERENCES `tags`(`id`) ON DELETE CASCADE
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_saved_item_tags_tagId` ON `saved_item_tags` (`tagId`)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `daily_app_usage` (
                        `dateLocal` TEXT NOT NULL,
                        `packageName` TEXT NOT NULL,
                        `usageMillis` INTEGER NOT NULL,
                        `sessionCount` INTEGER,
                        `pickupCount` INTEGER,
                        `sourceStatus` TEXT NOT NULL,
                        `lastCalculatedAtUtc` INTEGER NOT NULL,
                        PRIMARY KEY(`dateLocal`, `packageName`)
                    )
                """.trimIndent())
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_daily_app_usage_dateLocal` ON `daily_app_usage` (`dateLocal`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_daily_app_usage_packageName` ON `daily_app_usage` (`packageName`)")

                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `user_preferences` (
                        `id` INTEGER NOT NULL PRIMARY KEY,
                        `onboardingComplete` INTEGER NOT NULL DEFAULT 0,
                        `trackedAppsJson` TEXT NOT NULL,
                        `dailyGoalsJson` TEXT NOT NULL,
                        `trackingPausedUntilUtc` INTEGER,
                        `retentionMonths` INTEGER NOT NULL DEFAULT 12,
                        `remindersEnabled` INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())

                // Insert default singleton preferences row with popular social apps
                val defaultTrackedApps = "[\"com.instagram.android\",\"com.google.android.youtube\",\"com.android.chrome\",\"com.reddit.frontpage\",\"com.twitter.android\",\"com.zhiliaoapp.musically\"]"
                val defaultGoals = "{\"com.instagram.android\":45,\"com.google.android.youtube\":60,\"com.android.chrome\":30}"
                db.execSQL("""
                    INSERT OR IGNORE INTO `user_preferences` 
                    (`id`, `onboardingComplete`, `trackedAppsJson`, `dailyGoalsJson`, `trackingPausedUntilUtc`, `retentionMonths`, `remindersEnabled`)
                    VALUES (1, 0, '$defaultTrackedApps', '$defaultGoals', NULL, 12, 0)
                """.trimIndent())
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `collections` ADD COLUMN `iconToken` TEXT NOT NULL DEFAULT '📁'")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE `saved_items` ADD COLUMN `description` TEXT")
                db.execSQL("ALTER TABLE `saved_items` ADD COLUMN `siteName` TEXT")
                db.execSQL("ALTER TABLE `saved_items` ADD COLUMN `authorName` TEXT")
            }
        }

        fun getInstance(context: Context): RewinddDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RewinddDatabase::class.java,
                    "rewindd_vault.db"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .build()
                INSTANCE = instance
                instance
            }
        }

        fun createInMemory(context: Context): RewinddDatabase {
            return Room.inMemoryDatabaseBuilder(
                context.applicationContext,
                RewinddDatabase::class.java
            ).allowMainThreadQueries().build()
        }
    }
}

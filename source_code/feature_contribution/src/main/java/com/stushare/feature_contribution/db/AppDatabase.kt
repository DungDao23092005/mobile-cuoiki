package com.stushare.feature_contribution.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Thêm các Entity mới vào danh sách
@Database(
    entities = [
        UserProfileEntity::class,
        SavedDocumentEntity::class,
        NotificationEntity::class
    ],
    version = 2, // <--- ĐÃ SỬA: Tăng version lên 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Thêm các abstract fun cho DAO mới
    abstract fun userDao(): UserDao
    abstract fun savedDocumentDao(): SavedDocumentDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stushare_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
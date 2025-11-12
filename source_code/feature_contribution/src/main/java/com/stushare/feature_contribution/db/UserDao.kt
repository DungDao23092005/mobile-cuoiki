package com.stushare.feature_contribution.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profile WHERE userId = :id LIMIT 1")
    fun getProfile(id: String): Flow<UserProfileEntity?>

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()
}
package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SabhaDao {
    @Query("SELECT * FROM sabha_counters ORDER BY id ASC")
    fun getAllSabhas(): Flow<List<SabhaCounter>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSabha(sabha: SabhaCounter)

    @Update
    suspend fun updateSabha(sabha: SabhaCounter)

    @Delete
    suspend fun deleteSabha(sabha: SabhaCounter)

    @Query("DELETE FROM sabha_counters WHERE id = :id")
    suspend fun deleteSabhaById(id: Int)
}

@Dao
interface AzkarProgressDao {
    @Query("SELECT * FROM azkar_progress")
    fun getAllProgress(): Flow<List<AzkarProgress>>

    @Query("SELECT * FROM azkar_progress WHERE azkarId = :id")
    suspend fun getProgressForId(id: Int): AzkarProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: AzkarProgress)

    @Update
    suspend fun updateProgress(progress: AzkarProgress)

    @Query("UPDATE azkar_progress SET currentCount = 0, completed = 0")
    suspend fun resetAllProgress()
}

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE `key` = :key")
    suspend fun getSetting(key: String): UserSetting?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSetting(setting: UserSetting)

    @Query("SELECT * FROM user_settings WHERE `key` = :key")
    fun observeSetting(key: String): Flow<UserSetting?>
}

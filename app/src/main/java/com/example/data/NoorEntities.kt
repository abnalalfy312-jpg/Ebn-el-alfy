package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sabha_counters")
data class SabhaCounter(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val zikr: String,
    val count: Int = 0,
    val goal: Int = 33,
    val totalCount: Int = 0
)

@Entity(tableName = "azkar_progress")
data class AzkarProgress(
    @PrimaryKey val azkarId: Int,
    val currentCount: Int = 0,
    val completed: Boolean = false,
    val lastUpdatedDate: String = "" // To reset on a new day
)

@Entity(tableName = "user_settings")
data class UserSetting(
    @PrimaryKey val key: String,
    val value: String
)

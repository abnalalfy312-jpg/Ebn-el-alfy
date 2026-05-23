package com.example.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoorRepository(private val db: NoorDatabase) {

    private val sabhaDao = db.sabhaDao()
    private val azkarProgressDao = db.azkarProgressDao()
    private val userSettingsDao = db.userSettingsDao()

    val allSabhas: Flow<List<SabhaCounter>> = sabhaDao.getAllSabhas()
    val allAzkarProgress: Flow<List<AzkarProgress>> = azkarProgressDao.getAllProgress()

    suspend fun getSelectedCity(): CityConfig {
        val setting = userSettingsDao.getSetting("selected_city_id")
        val cityId = setting?.value ?: "cairo"
        return NoorPresetData.cities.find { it.id == cityId } ?: NoorPresetData.cities.first()
    }

    suspend fun saveSelectedCity(cityId: String) {
        userSettingsDao.saveSetting(UserSetting("selected_city_id", cityId))
    }

    suspend fun getCalculationMethodName(): String {
        return userSettingsDao.getSetting("calc_method")?.value ?: "AUTO"
    }

    suspend fun saveCalculationMethodName(methodName: String) {
        userSettingsDao.saveSetting(UserSetting("calc_method", methodName))
    }

    suspend fun incrementSabha(id: Int, incrementTotal: Boolean = true) {
        val sabhas = allSabhas.firstOrNull() ?: return
        val item = sabhas.find { it.id == id } ?: return
        
        var nextCount = item.count + 1
        var nextTotal = item.totalCount + (if (incrementTotal) 1 else 0)
        
        if (nextCount > item.goal) {
            nextCount = 1 // Reset list loop
        }
        
        sabhaDao.updateSabha(item.copy(count = nextCount, totalCount = nextTotal))
    }

    suspend fun resetSabhaCount(id: Int) {
        val sabhas = allSabhas.firstOrNull() ?: return
        val item = sabhas.find { it.id == id } ?: return
        sabhaDao.updateSabha(item.copy(count = 0))
    }

    suspend fun addNewSabha(zikr: String, goal: Int) {
        sabhaDao.insertSabha(SabhaCounter(zikr = zikr, goal = goal))
    }

    suspend fun deleteSabha(id: Int) {
        sabhaDao.deleteSabhaById(id)
    }

    // Azkar methods
    suspend fun incrementAzkarProgress(azkarId: Int, maxCount: Int): AzkarProgress {
        val currentDate = getCurrentDateString()
        val existing = azkarProgressDao.getProgressForId(azkarId)
        
        val updated = if (existing == null || existing.lastUpdatedDate != currentDate) {
            AzkarProgress(
                azkarId = azkarId,
                currentCount = 1,
                completed = maxCount <= 1,
                lastUpdatedDate = currentDate
            )
        } else {
            val nextCount = existing.currentCount + 1
            existing.copy(
                currentCount = nextCount,
                completed = nextCount >= maxCount,
                lastUpdatedDate = currentDate
            )
        }
        azkarProgressDao.insertProgress(updated)
        return updated
    }

    suspend fun resetAzkarForToday() {
        azkarProgressDao.resetAllProgress()
    }

    suspend fun checkAndResetAzkarIfNewDay() {
        val currentDate = getCurrentDateString()
        val lastResetSetting = userSettingsDao.getSetting("last_reset_date")
        
        if (lastResetSetting == null || lastResetSetting.value != currentDate) {
            azkarProgressDao.resetAllProgress()
            userSettingsDao.saveSetting(UserSetting("last_reset_date", currentDate))
        }
    }

    // Generic setting helpers
    suspend fun getSettingValue(key: String, defaultValue: String): String {
        return userSettingsDao.getSetting(key)?.value ?: defaultValue
    }

    suspend fun saveSettingValue(key: String, value: String) {
        userSettingsDao.saveSetting(UserSetting(key, value))
    }

    // Simple date getter
    fun getCurrentDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    // Seeding on first run
    suspend fun seedDatabaseIfNeeded() {
        val sabhaList = sabhaDao.getAllSabhas().firstOrNull()
        if (sabhaList.isNullOrEmpty()) {
            // Seed defaults
            NoorPresetData.defaultSabhas.forEachIndexed { i, zikr ->
                sabhaDao.insertSabha(
                    SabhaCounter(
                        zikr = zikr,
                        count = 0,
                        goal = if (i == 2 || i == 4 || i == 7) 100 else 33,
                        totalCount = 0
                    )
                )
            }
        }
        
        // Seed first day reset config
        userSettingsDao.saveSetting(UserSetting("last_reset_date", getCurrentDateString()))
    }
}

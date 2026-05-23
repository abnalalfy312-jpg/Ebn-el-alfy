package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class NoorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoorRepository

    // Central states
    private val _selectedCity = MutableStateFlow(NoorPresetData.cities.first())
    val selectedCity: StateFlow<CityConfig> = _selectedCity.asStateFlow()

    private val _prayerTimes = MutableStateFlow<PrayerTimesCalculator.PrayerTimes?>(null)
    val prayerTimes: StateFlow<PrayerTimesCalculator.PrayerTimes?> = _prayerTimes.asStateFlow()

    private val _nextPrayerName = MutableStateFlow("الفجر")
    val nextPrayerName: StateFlow<String> = _nextPrayerName.asStateFlow()

    private val _nextPrayerTimeLeft = MutableStateFlow("00:00:00")
    val nextPrayerTimeLeft: StateFlow<String> = _nextPrayerTimeLeft.asStateFlow()

    // Room Database Observables
    val sabhaList: StateFlow<List<SabhaCounter>>
    val azkarProgress: StateFlow<List<AzkarProgress>>

    private val _selectedSabhaId = MutableStateFlow<Int?>(null)
    val selectedSabhaId: StateFlow<Int?> = _selectedSabhaId.asStateFlow()

    // New Settings States
    private val _preferredAzan = MutableStateFlow("abdel_basit")
    val preferredAzan: StateFlow<String> = _preferredAzan.asStateFlow()

    private val _morningReminderEnabled = MutableStateFlow(true)
    val morningReminderEnabled: StateFlow<Boolean> = _morningReminderEnabled.asStateFlow()

    private val _morningReminderTime = MutableStateFlow("06:00")
    val morningReminderTime: StateFlow<String> = _morningReminderTime.asStateFlow()

    private val _eveningReminderEnabled = MutableStateFlow(true)
    val eveningReminderEnabled: StateFlow<Boolean> = _eveningReminderEnabled.asStateFlow()

    private val _eveningReminderTime = MutableStateFlow("17:00")
    val eveningReminderTime: StateFlow<String> = _eveningReminderTime.asStateFlow()

    // Active screen navigation
    private val _activeTab = MutableStateFlow(0) // 0: Prayer, 1: Azkar, 2: Sabha
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private var timerJob: Job? = null

    init {
        val database = NoorDatabase.getDatabase(application)
        repository = NoorRepository(database)

        sabhaList = repository.allSabhas.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        azkarProgress = repository.allAzkarProgress.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            // Prepopulate data if first launch
            repository.seedDatabaseIfNeeded()
            repository.checkAndResetAzkarIfNewDay()
            
            // Load settings
            _preferredAzan.value = repository.getSettingValue("preferred_azan_sound", "abdel_basit")
            _morningReminderEnabled.value = repository.getSettingValue("morning_azkar_reminder_enabled", "true") == "true"
            _morningReminderTime.value = repository.getSettingValue("morning_azkar_reminder_time", "06:00")
            _eveningReminderEnabled.value = repository.getSettingValue("evening_azkar_reminder_enabled", "true") == "true"
            _eveningReminderTime.value = repository.getSettingValue("evening_azkar_reminder_time", "17:00")

            // Load selected city
            val savedCity = repository.getSelectedCity()
            _selectedCity.value = savedCity
            
            // Generate prayer times
            recalculatePrayerTimes(savedCity)
            
            // Autoselect first sabha if available
            sabhaList.collect { list ->
                if (_selectedSabhaId.value == null && list.isNotEmpty()) {
                    _selectedSabhaId.value = list.first().id
                }
            }
        }

        // Start countdown timer ticker
        startTimer()
    }

    fun setActiveTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    // Settings Mutators
    fun setPreferredAzan(soundId: String) {
        viewModelScope.launch {
            repository.saveSettingValue("preferred_azan_sound", soundId)
            _preferredAzan.value = soundId
        }
    }

    fun setMorningReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveSettingValue("morning_azkar_reminder_enabled", enabled.toString())
            _morningReminderEnabled.value = enabled
        }
    }

    fun setMorningReminderTime(time: String) {
        viewModelScope.launch {
            repository.saveSettingValue("morning_azkar_reminder_time", time)
            _morningReminderTime.value = time
        }
    }

    fun setEveningReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.saveSettingValue("evening_azkar_reminder_enabled", enabled.toString())
            _eveningReminderEnabled.value = enabled
        }
    }

    fun setEveningReminderTime(time: String) {
        viewModelScope.launch {
            repository.saveSettingValue("evening_azkar_reminder_time", time)
            _eveningReminderTime.value = time
        }
    }

    fun selectCity(city: CityConfig) {
        viewModelScope.launch {
            repository.saveSelectedCity(city.id)
            _selectedCity.value = city
            recalculatePrayerTimes(city)
        }
    }

    private fun recalculatePrayerTimes(city: CityConfig) {
        val calculator = PrayerTimesCalculator(
            latitude = city.latitude,
            longitude = city.longitude,
            timezone = city.timezone,
            method = city.method
        )
        val today = Calendar.getInstance()
        _prayerTimes.value = calculator.calculateTimes(today)
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                _prayerTimes.value?.let { times ->
                    val (name, countdown) = getNextPrayerCountdown(times)
                    _nextPrayerName.value = name
                    _nextPrayerTimeLeft.value = countdown
                }
                delay(1000)
            }
        }
    }

    private fun getNextPrayerCountdown(times: PrayerTimesCalculator.PrayerTimes): Pair<String, String> {
        val now = Calendar.getInstance()
        val nowHour = now.get(Calendar.HOUR_OF_DAY)
        val nowMinute = now.get(Calendar.MINUTE)
        val nowSecond = now.get(Calendar.SECOND)
        val nowInSeconds = nowHour * 3600 + nowMinute * 60 + nowSecond

        val prayers = listOf(
            Pair("الفجر", times.fajr),
            Pair("الظهر", times.dhuhr),
            Pair("العصر", times.asr),
            Pair("المغرب", times.maghrib),
            Pair("العشاء", times.isha)
        )

        var nextPrayerName = ""
        var remainingSeconds = 0

        for (p in prayers) {
            val parts = p.second.split(":")
            if (parts.size == 2) {
                val pHour = parts[0].toIntOrNull() ?: 0
                val pMinute = parts[1].toIntOrNull() ?: 0
                val pInSeconds = pHour * 3600 + pMinute * 60

                if (pInSeconds > nowInSeconds) {
                    nextPrayerName = p.first
                    remainingSeconds = pInSeconds - nowInSeconds
                    break
                }
            }
        }

        // If no prayer left today, next prayer is Fajr tomorrow
        if (nextPrayerName.isEmpty()) {
            nextPrayerName = "الفجر"
            val parts = times.fajr.split(":")
            if (parts.size == 2) {
                val pHour = parts[0].toIntOrNull() ?: 0
                val pMinute = parts[1].toIntOrNull() ?: 0
                val pInSeconds = pHour * 3600 + pMinute * 60
                remainingSeconds = (24 * 3600 - nowInSeconds) + pInSeconds
            }
        }

        val h = remainingSeconds / 3600
        val m = (remainingSeconds % 3600) / 60
        val s = remainingSeconds % 60
        val countdownStr = String.format("%02d:%02d:%02d", h, m, s)

        return Pair(nextPrayerName, countdownStr)
    }

    // Sabha controls
    fun selectSabha(id: Int) {
        _selectedSabhaId.value = id
    }

    fun incrementActiveSabha() {
        val activeId = _selectedSabhaId.value ?: return
        viewModelScope.launch {
            repository.incrementSabha(activeId)
        }
    }

    fun resetActiveSabha() {
        val activeId = _selectedSabhaId.value ?: return
        viewModelScope.launch {
            repository.resetSabhaCount(activeId)
        }
    }

    fun addCustomSabha(zikr: String, goal: Int) {
        viewModelScope.launch {
            repository.addNewSabha(zikr, goal)
        }
    }

    fun deleteSabha(id: Int) {
        viewModelScope.launch {
            if (_selectedSabhaId.value == id) {
                _selectedSabhaId.value = null
            }
            repository.deleteSabha(id)
            
            // Choose next available as selected
            sabhaList.value.firstOrNull { it.id != id }?.let {
                _selectedSabhaId.value = it.id
            }
        }
    }

    // Azkar controls
    fun incrementAzkar(id: Int, maxCount: Int) {
        viewModelScope.launch {
            repository.incrementAzkarProgress(id, maxCount)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

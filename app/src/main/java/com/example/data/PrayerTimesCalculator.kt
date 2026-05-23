package com.example.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.*

class PrayerTimesCalculator(
    private val latitude: Double,
    private val longitude: Double,
    private val timezone: Double,
    private val method: CalculationMethod = CalculationMethod.UMM_AL_QURA
) {
    enum class CalculationMethod(val fajrAngle: Double, val ishaAngle: Double, val ishaInterval: Int = 0) {
        UMM_AL_QURA(18.5, 0.0, 90), // 18.5 deg Fajr, Isha is 90 mins after Maghrib (120 in Ramadan)
        EGYPT_SURVEY(19.5, 17.5, 0),
        MUSLIM_WORLD_LEAGUE(18.0, 17.0, 0),
        ISNA(15.0, 15.0, 0),
        KARACHI(18.0, 18.0, 0),
        GULF_METHOD(19.5, 0.0, 90)
    }

    data class PrayerTimes(
        val fajr: String,
        val sunrise: String,
        val dhuhr: String,
        val asr: String,
        val maghrib: String,
        val isha: String
    )

    fun calculateTimes(calendar: Calendar): PrayerTimes {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // 1. Calculate Julian Date
        val jd = julianDate(year, month, day)

        // 2. Compute solar Declination, Equation of Time
        val djd = jd - 2451545.0 // days since anomaly epoch
        val g = (357.529 + 0.98560028 * djd) % 360.0
        val q = (280.459 + 0.98564736 * djd) % 360.0
        val L = (q + 1.915 * sin(rad(g)) + 0.020 * sin(rad(2 * g))) % 360.0
        
        val e = 23.439 - 0.00000036 * djd
        val decl = deg(asin(sin(rad(e)) * sin(rad(L))))
        
        var RA = deg(atan2(cos(rad(e)) * sin(rad(L)), cos(rad(L)))) / 15.0
        if (RA < 0) RA += 24.0
        
        val EqT = q/15.0 - RA

        // 3. Mid Day (Dhuhr)
        var noon = 12.0 + timezone - longitude / 15.0 - EqT
        if (noon < 0) noon += 24.0
        if (noon >= 24) noon -= 24.0

        // 4. Sunrise & Sunset Angle (-0.833)
        val sunriseSunsetHourAngle = hourAngle(latitude, -0.833, decl)
        val sunriseTime = noon - sunriseSunsetHourAngle / 15.0
        val sunsetTime = noon + sunriseSunsetHourAngle / 15.0

        // 5. Fajr
        val fajrAngle = method.fajrAngle
        val fajrHourAngle = hourAngle(latitude, -fajrAngle, decl)
        val fajrTime = noon - fajrHourAngle / 15.0

        // 6. Isha
        val ishaTime = if (method.ishaAngle > 0.1) {
            val ishaHourAngle = hourAngle(latitude, -method.ishaAngle, decl)
            noon + ishaHourAngle / 15.0
        } else {
            sunsetTime + (method.ishaInterval / 60.0)
        }

        // 7. Asr (Standard shadow ratio = 1)
        val asrAltitude = deg(atan(1.0 / (1.0 + tan(rad(abs(latitude - decl))))))
        val asrHourAngle = hourAngle(latitude, asrAltitude, decl)
        val asrTime = noon + asrHourAngle / 15.0

        return PrayerTimes(
            fajr = formatTime(fajrTime),
            sunrise = formatTime(sunriseTime),
            dhuhr = formatTime(noon),
            asr = formatTime(asrTime),
            maghrib = formatTime(sunsetTime),
            isha = formatTime(ishaTime)
        )
    }

    private fun julianDate(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val A = floor(y / 100.0)
        val B = 2 - A + floor(A / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + B - 1524.5
    }

    private fun hourAngle(lat: Double, alt: Double, decl: Double): Double {
        val latRad = rad(lat)
        val declRad = rad(decl)
        val altRad = rad(alt)
        
        val cosH = (sin(altRad) - sin(latRad) * sin(declRad)) / (cos(latRad) * cos(declRad))
        if (cosH > 1.0) return 0.0
        if (cosH < -1.0) return 180.0
        return deg(acos(cosH))
    }

    private fun rad(deg: Double) = deg * Math.PI / 180.0
    private fun deg(rad: Double) = rad * 180.0 / Math.PI

    private fun formatTime(time: Double): String {
        var t = time
        if (t.isNaN()) return "00:00"
        while (t < 0) t += 24.0
        while (t >= 24) t -= 24.0
        val hours = floor(t).toInt()
        val minutes = floor((t - hours) * 60 + 0.5).toInt()
        
        var h = hours
        var m = minutes
        if (m >= 60) {
            m %= 60
            h = (h + 1) % 24
        }
        return String.format("%02d:%02d", h, m)
    }
}

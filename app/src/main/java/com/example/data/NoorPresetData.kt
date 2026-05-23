package com.example.data

data class CityConfig(
    val id: String,
    val arabicName: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: Double,
    val method: PrayerTimesCalculator.CalculationMethod
)

data class AzkarItem(
    val id: Int,
    val text: String,
    val description: String,
    val count: Int,
    val category: String,
    val title: String
)

object NoorPresetData {
    val cities = listOf(
        CityConfig("cairo", "القاهرة، مصر", 30.0444, 31.2357, 3.0, PrayerTimesCalculator.CalculationMethod.EGYPT_SURVEY),
        CityConfig("mecca", "مكة المكرمة، السعودية", 21.3891, 39.8579, 3.0, PrayerTimesCalculator.CalculationMethod.UMM_AL_QURA),
        CityConfig("riyadh", "الرياض، السعودية", 24.7136, 46.6753, 3.0, PrayerTimesCalculator.CalculationMethod.UMM_AL_QURA),
        CityConfig("medina", "المدينة المنورة، السعودية", 24.4672, 39.6111, 3.0, PrayerTimesCalculator.CalculationMethod.UMM_AL_QURA),
        CityConfig("jerusalem", "القدس الشريف، فلسطين", 31.7683, 35.2137, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("dubai", "دبي، الإمارات", 25.2048, 55.2708, 4.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("abu_dhabi", "أبو ظبي، الإمارات", 24.4539, 54.3773, 4.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("amman", "عمان، الأردن", 31.9522, 35.9106, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("baghdad", "بغداد، العراق", 33.3152, 44.3660, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("kuwait", "الكويت العاصمة، الكويت", 29.3759, 47.9774, 3.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("doha", "الدوحة، قطر", 25.2854, 51.5310, 3.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("manama", "المنامة، البحرين", 26.2285, 50.5860, 3.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("muscat", "مسقط، عمان", 23.5859, 58.4059, 4.0, PrayerTimesCalculator.CalculationMethod.GULF_METHOD),
        CityConfig("damascus", "دمشق، سوريا", 33.5138, 36.2765, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("beirut", "بيروت، لبنان", 33.8938, 35.5018, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("sanaa", "صنعاء، اليمن", 15.3694, 44.1910, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("tripoli", "طرابلس، ليبيا", 32.8872, 13.1913, 2.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("tunis", "تونس العاصمة، تونس", 36.8065, 10.1815, 1.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("algiers", "الجزائر العاصمة، الجزائر", 36.7525, 3.0420, 1.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("rabat", "الرباط، المغرب", 34.0209, -6.8416, 1.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("khartoum", "الخرطوم، السودان", 15.5007, 32.5599, 2.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE),
        CityConfig("istanbul", "إسطنبول، تركيا", 41.0082, 28.9784, 3.0, PrayerTimesCalculator.CalculationMethod.MUSLIM_WORLD_LEAGUE)
    )

    val azkarItems = listOf(
        // أذكار الصباح
        AzkarItem(
            id = 1,
            title = "آية الكرسي",
            text = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَؤُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            description = "من قالها حين يصبح أجير من الجن حتى يمسي.",
            count = 1,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 2,
            title = "المعوذات (ثلاث مرات)",
            text = "سورة الإخلاص، سورة الفلق، وسورة الناس.",
            description = "من قالها ثلاثاً حين يصبح وحين يمسي كفته من كل شيء.",
            count = 3,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 3,
            title = "سيد الاستغفار",
            text = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            description = "من قالها موقناً بها فمات من يومه قبل أن يمسي فهو من أهل الجنة.",
            count = 1,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 4,
            title = "الصلاة على النبي",
            text = "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ",
            description = "من صلى عليّ حين يصبح عشراً وحين يمسي عشراً أدركته شفاعتي يوم القيامة.",
            count = 10,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 5,
            title = "الرضا بالله رباً والنبى نبيا",
            text = "رَضِيتُ بِاللَّهِ رَبَّاً، وَبِالْإِسْلَامِ دِينَاً، وَبِمُحَمَّدٍ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ نَبِيَّاً",
            description = "من قالها ثلاثاً حين يصبح وثلاثاً حين يمسي كان حقاً على الله كرمه أن يرضيه.",
            count = 3,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 6,
            title = "التحصين اليومي",
            text = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ",
            description = "من قالها ثلاثاً لم يضره شيء في ذلك اليوم أو تلك الليلة.",
            count = 3,
            category = "أذكار الصباح"
        ),
        AzkarItem(
            id = 7,
            title = "كفاية الهم",
            text = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
            description = "من قالها سبع مرات كفاه الله ما أهمه من أمر الدنيا والآخرة.",
            count = 7,
            category = "أذكار الصباح"
        ),

        // أذكار المساء
        AzkarItem(
            id = 8,
            title = "آية الكرسي للمساء",
            text = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ...",
            description = "من قالها حين يمسي أجير من الجن حتى يصبح.",
            count = 1,
            category = "أذكار المساء"
        ),
        AzkarItem(
            id = 9,
            title = "الاستعاذة بكلمات الله التامات",
            text = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ",
            description = "من قالها ثلاثاً حين يمسي لم تضره حمة (سمّ أو لدغة عقرب) تلك الليلة.",
            count = 3,
            category = "أذكار المساء"
        ),
        AzkarItem(
            id = 10,
            title = "أذكار الحمد والملك",
            text = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ. رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا.",
            description = "يقال دبر اليوم وخلال دخول المساء المبارك للتذكير بالملكوت والتوكل.",
            count = 1,
            category = "أذكار المساء"
        ),
        AzkarItem(
            id = 11,
            title = "غفران الذنوب",
            text = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ",
            description = "من قالها مائة مرة حطت خطاياه وإن كانت مثل زبد البحر.",
            count = 100,
            category = "أذكار المساء"
        ),

        // أذكار بعد الصلاة
        AzkarItem(
            id = 12,
            title = "الاستغفار بعد السلام",
            text = "أَسْتَغْفِرُ اللَّهَ (ثلاث مرات).. اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالْإِكْرَامِ",
            description = "السنة الثابتة عن النبي صلى الله عليه وسلم فور الانتهاء من الصلاة.",
            count = 1,
            category = "أذكار بعد الصلاة"
        ),
        AzkarItem(
            id = 13,
            title = "التسبيح والتحميد والتكبير",
            text = "سُبْحَانَ اللهِ (33 مرة) ، الحَمْدُ للهِ (33 مرة) ، اللهُ أكْبَرُ (33 مرة) .. ثم تمام المائة: لَا إلَهَ إلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
            description = "من سبح دبر كل صلاة مكتوبة غفرت خطاياه وإن كانت مثل زبد البحر.",
            count = 34, // 33 for subhan/hamd/takbeer, unified card
            category = "أذكار بعد الصلاة"
        )
    )

    val defaultSabhas = listOf(
        "سُبْحَانَ اللَّهِ",
        "الْحَمْدُ لِلَّهِ",
        "لَا إِلَهَ إِلَّا اللَّهُ",
        "اللَّهُ أَكْبَرُ",
        "أَسْتَغْفِرُ اللَّهَ",
        "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
        "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَآلِ مُحَمَّدٍ",
        "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ سُبْحَانَ اللَّهِ الْعَظِيمِ"
    )
}

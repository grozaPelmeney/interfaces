package com.example

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import com.example.interfaces.R
import com.example.interfaces.models.*

object TestDataGenerator {
    fun getContent(type: ContentType? = null): List<Content> {
        val content = listOf(
            Content(
                0,
                "СТС",
                imgUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c1/%D0%9B%D0%BE%D0%B3%D0%BE%D1%82%D0%B8%D0%BF_%D0%A1%D0%A2%D0%A1_2005-2012.png",
                type = ContentType.TV_CHANNEL
            ),
            Content(
                1,
                "ТНТ",
                imgUrl = "https://sun6-20.userapi.com/s/v1/if1/PvOC0xwn_iTs9GG8RTloPA6JHcOynmhU8u5fJUNwhUJKntIZj2Ll5k07f33PC3xbRo_1uoX_.jpg?size=800x800&quality=96&crop=0,0,800,800&ava=1",
                type = ContentType.TV_CHANNEL
            ),
            Content(
                2,
                "2х2",
                imgUrl = "https://assets.faceit-cdn.net/hubs/avatar/b4c5fe60-3e4f-4e67-a5f9-34b6569b444a_1579521440730.jpg",
                type = ContentType.TV_CHANNEL
            ),
            Content(
                3,
                "YouTube",
                imgUrl = "https://i.pinimg.com/originals/de/1c/91/de1c91788be0d791135736995109272a.png",
                type = ContentType.SITE
            ),
            Content(
                4,
                "TikTok",
                imgUrl = "https://cdn141.picsart.com/305061469080211.png",
                type = ContentType.SITE
            ),
            Content(
                5,
                "Яндекс Музыка",
                imgUrl = "https://camo.githubusercontent.com/0b43a64eb5b84a826a035fc9c9759d4b2929670a4c3d2f4a886be1b4b00e3cff/68747470733a2f2f75706c6f61642e77696b696d656469612e6f72672f77696b6970656469612f636f6d6d6f6e732f7468756d622f652f65372f59616e6465785f4d757369635f69636f6e2e7376672f3230343870782d59616e6465785f4d757369635f69636f6e2e7376672e706e67",
                type = ContentType.MUSIC
            ),
            Content(
                6,
                "Spotyfy",
                imgUrl = "https://www.pngall.com/wp-content/uploads/9/Spotify-Logo.png",
                type = ContentType.MUSIC
            ),
            Content(
                7,
                "Doodle jump",
                imgUrl = "https://m.media-amazon.com/images/I/51-FgcxMf6L._AC_UL800_QL65_.png",
                type = ContentType.GAME
            ),
            Content(
                8,
                "АвтоПлюс",
                imgUrl = "https://sun6-22.userapi.com/s/v1/if1/i2z92T7PhhLfCvYtnvgwOzlljTR3xZyOO9Pqzpv_veXTsCRUOYbh9MVTWIJ1MfUZGjoELff-.jpg?size=929x1103&quality=96&crop=39,39,929,1103&ava=1",
                type = ContentType.TV_CHANNEL
            ),
            Content(
                9,
                "Домашний",
                imgUrl = "https://head-media.ru/wp-content/uploads/2020/08/dommashnii.jpg",
                type = ContentType.TV_CHANNEL
            ),
            Content(
                10,
                "Кинопоиск",
                imgUrl = "https://cdn-1.webcatalog.io/catalog/kinopoisk/kinopoisk-icon.png",
                type = ContentType.SITE
            ),
            Content(
                11,
                "Jetpack Joyride",
                imgUrl = "https://www.game-ost.ru/static/covers_soundtracks/4/5/452312_86057.jpg",
                type = ContentType.GAME
            ),
            Content(
                12,
                "Hill Climb Racing",
                imgUrl = "https://is4-ssl.mzstatic.com/image/thumb/Purple122/v4/9b/3b/49/9b3b49d3-fc63-140f-269b-922b8959fea9/AppIcon-0-0-1x_U007emarketing-0-0-0-4-0-0-sRGB-0-0-0-GLES2_U002c0-512MB-85-220-0-0.png/1024x1024bb.png",
                type = ContentType.GAME
            ),
            Content(
                13,
                "Angry Birds",
                imgUrl = "https://toppng.com/uploads/preview/angry-birds-logo-11549951611ntbndxbupv.png",
                type = ContentType.GAME
            ),
            Content(
                14,
                "Subway Surfers",
                imgUrl = "https://static.wikia.nocookie.net/subwaysurf/images/4/44/SaoPauloIcon.webp/revision/latest?cb=20220327093023&path-prefix=ru",
                type = ContentType.GAME
            ),
            Content(
                15,
                "Traffic Racer",
                imgUrl = "https://play-lh.googleusercontent.com/rQP_gNOU0bhBr11x-MfbcbRrsRZ0UMvDy6fIignIFXuo58QD1v3FNnJj4hwfSF-fR6LG",
                type = ContentType.GAME
            ),
            Content(
                16,
                "Super Mario",
                imgUrl = "https://i.pinimg.com/originals/9c/1b/8f/9c1b8f95f6cad0d1ec848b38a450ff58.png",
                type = ContentType.GAME
            ),
            Content(
                17,
                "Mario Cart",
                imgUrl = "https://www.pidgi.net/wiki/images/9/96/Mario_%28alt_2%29_-_Mario_Kart_Super_Circuit.jpg",
                type = ContentType.GAME
            ),
            Content(
                18,
                "Plants vs zombies",
                imgUrl = "https://all-t-shirts.ru/goods_images/1713/1876/ru110082/ru110082II0009531270b4a60ba6ea23345074c1c612b.jpg",
                type = ContentType.GAME
            ),
        )

        return if (type == null) content
               else content.filter { it.type == type }
    }

    fun getUsers(): List<User> {
        val content = getContent()

        val son1Likes = content.filter { listOf(0, 2, 13, 18, 3, 7).contains(it.id) }
        val son2Likes = content.filter { listOf(1, 4, 6).contains(it.id) }
        val momLikes = content.filter { listOf(4, 5, 9).contains(it.id) }
        val fatherLikes = content.filter { listOf(2, 3, 8, 6).contains(it.id) }

        return listOf(
            User(0, "Сын1").apply {
                likes = son1Likes
            },
            User(1, "Сын2").apply {
                likes = son2Likes
            },
            User(2, "Мама").apply {
                likes = momLikes
            },
            User(3, "Папа").apply {
                likes = fatherLikes
            },
        )
    }

    fun getSchedules(): List<Setting> {
        val days = listOf(R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday, R.string.sunday)

        val schedules = mutableListOf<Setting>()

        days.forEach { dayNameRes ->
            schedules.add(
                Setting.CheckWithTimeSetting(
                    id = 1,
                    nameRes = dayNameRes,
                    isSelected = false,
                    value = ""
                )
            )
        }

        return schedules
    }

    fun getSettings(): List<Setting> {
        val settings = mutableListOf<Setting>()

        val languageValues = listOf(
            SelectSettingItem(
                id = 0,
                nameRes = R.string.language_ru
            ),
            SelectSettingItem(
                id = 1,
                nameRes = R.string.language_en
            ),
            SelectSettingItem(
                id = 2,
                nameRes = R.string.language_fr
            ),
            SelectSettingItem(
                id = 3,
                nameRes = R.string.language_de
            ),
        )
        val languageSetting =
            Setting.SelectSetting(
                id = 0,
                type = SettingType.SELECT,
                nameRes = R.string.settings_language,
                selectedValueId = 0,
                values = languageValues
            )
        settings.add(languageSetting)

        val brightnessSetting =
            Setting.RangeSetting(
                id = 10,
                type = SettingType.RANGE,
                nameRes = R.string.settings_brightness,
                from = 0,
                to = 100,
                value = 50
            )
        settings.add(brightnessSetting)

        val contrastSetting =
            Setting.RangeSetting(
                id = 20,
                type = SettingType.RANGE,
                nameRes = R.string.settings_contrast,
                from = 0,
                to = 100,
                value = 50
            )
        settings.add(contrastSetting)

        val sharpnessSetting =
            Setting.RangeSetting(
                id = 30,
                type = SettingType.RANGE,
                nameRes = R.string.settings_sharpness,
                from = 0,
                to = 100,
                value = 50
            )
        settings.add(sharpnessSetting)

        val colorSetting =
            Setting.RangeSetting(
                id = 40,
                type = SettingType.RANGE,
                nameRes = R.string.settings_color,
                from = 0,
                to = 100,
                value = 50
            )
        settings.add(colorSetting)

        return settings
    }

    fun getTvPrograms(): List<String> {
        val programs = listOf("Трансформеры", "Сверхъестественное", "Во все тяжкие", "Первому игроку  приготовиться", "Неудержимые", "Рик и Морти", "Minecraft шоу", "Великолепный век", "С приветом по планетaм", "Воронины")
        return programs.shuffled()
    }

    fun getTvProgramText(): Spanned {
        val text = StringBuilder()

        val times = listOf("00:00 - 06:00", "06:00 - 07:00", "07:00 - 10:00", "10:00 - 12:00", "12:00 - 15:00", "15:00 - 17:00", "17:00 - 20:00", "20:00 - 22:00", "22:00 - 00:00")

        times.forEachIndexed { i, time ->
            val program =
                when (i) {
                    0 -> "Музыка"
                    1 -> "Новости"
                    else -> getTvPrograms().random()
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                text.append("<b>$program</b><br>")
                text.append("$time<br><br>")
            } else {
                text.append("$program\n")
                text.append("$time\n\n")
            }
        }

        val spannedText =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text.toString(), 0)
            } else {
                SpannableString.valueOf(text)
            }

        return spannedText
    }
}
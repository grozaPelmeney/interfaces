package com.example.interfaces.models

import com.example.interfaces.R

enum class ContentType(val value: Int, val titleResId: Int) {
    FAVOURITES(0, R.string.content_favourites),
    TV_CHANNEL(1, R.string.content_channels),
    GAME(2, R.string.content_games),
    SITE(3, R.string.content_sites),
    MUSIC(4, R.string.content_music),
}
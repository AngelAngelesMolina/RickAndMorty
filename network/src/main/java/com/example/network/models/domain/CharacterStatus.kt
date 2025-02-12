package com.example.network.models.domain

import android.graphics.Color

sealed class CharacterStatus(val displayName: String) {
    object Alive: CharacterStatus("Alive")
    object Dead: CharacterStatus("Dead")
    object Unknown: CharacterStatus("Unknown")
}
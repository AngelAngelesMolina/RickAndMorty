package com.example.rickandmorty.utils

import androidx.compose.ui.graphics.Color
import com.example.network.models.domain.CharacterStatus


fun CharacterStatus.asColor(): Color {
    return when (this) {
        CharacterStatus.Alive -> Color.Green
        CharacterStatus.Dead -> Color.Red
        CharacterStatus.Unknown -> Color.Yellow
    }
}
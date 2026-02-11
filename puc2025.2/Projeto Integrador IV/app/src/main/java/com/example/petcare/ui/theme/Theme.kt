package com.example.petcare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 🌙 Paleta modo escuro
private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    onPrimary = White,
    secondary = GreenLight,
    onSecondary = White,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White
)

// ☀️ Paleta modo claro (principal do PetCare)
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,      // Botões e elementos principais
    onPrimary = White,           // Texto sobre botões verdes
    secondary = GreenLight,      // Acentos, detalhes
    onSecondary = White,
    background = Background,     // Fundo geral da tela
    onBackground = GrayText,     // Texto padrão
    surface = White,             // Cartões, botões, etc.
    onSurface = GrayText         // Texto em superfícies claras
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

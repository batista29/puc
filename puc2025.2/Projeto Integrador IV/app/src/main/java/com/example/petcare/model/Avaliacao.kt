package com.example.petcare.model
data class Avaliacao(
    val comentario: String = "",
    val data: String = "",         // Firestore → string
    val nota: String = "",         // Firestore → string
    val tutorId: String = "",
    val veterinarioId: String = ""
)
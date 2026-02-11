package com.example.petcare.model

import com.google.firebase.Timestamp

data class Consulta(
    val id: String = "",
    val tutorId: String = "",
    val petOwnerId: String = "",
    val veterinarioNome: String = "",
    val veterinarioId: String = "",
    val petNome: String = "",
    val servico: String = "",
    val data: String = "",
    val hora: String = "",
    val status: String = "Pendente",
    val createdAt: Timestamp = Timestamp.now()
)

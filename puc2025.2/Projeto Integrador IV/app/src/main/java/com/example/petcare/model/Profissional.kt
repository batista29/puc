package com.example.petcare.model

data class Profissional(
    val nome: String = "",
    val cidade: String = "",
    val nota: Double = 0.0,
    val imagem: String = "",
    val email: String = "",
    val telefone: String = "",
    val cep: String = "",
    val especialidades: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val fotoUrl: String = "", // Mapeado de 'fotoUrl'
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val disponibilidade: String = "",
    val tipo: String = "",// Para filtrar se é profissional/tutor
    val descricao: String = "",
    val endereco: String = "",
    val bairro: String = "",
)
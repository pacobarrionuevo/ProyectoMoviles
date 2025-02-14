package com.example.aplicacionconciertos.model.activities

import java.time.LocalDate

data class ActivityResponse(
    val id: Long,
    val name: String,
    val description: String,
    val date: LocalDate,
    val place: String,
    val category: String
)
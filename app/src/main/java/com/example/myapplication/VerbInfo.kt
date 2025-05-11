package com.example.myapplication

data class ConjugationResponse(
    val success: Boolean,
    val data: Map<String, TenseData>
)

data class TenseData(
    val S1: List<String>,
    val S2: List<String>,
    val S3: List<String>,
    val P1: List<String>,
    val P2: List<String>,
    val P3: List<String>
)

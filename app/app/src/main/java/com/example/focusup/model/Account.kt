package com.example.focusup.model

data class Account(
    val id: Int,
    val name: String,
    val email: String,
    val password: String, 
    val isPremium: Boolean,
    val createdAt: String
)
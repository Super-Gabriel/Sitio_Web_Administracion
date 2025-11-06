package com.focusup.focusupapp.model

data class Account(
    val id: Int,
    val name: String,
    val email: String,
    val password: String, 
    val isPremium: Boolean,
    var points: Int,
    val createdAt: String
)
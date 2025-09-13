package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseUser(
    val email: String = "",
    val displayName: String = "",
    val level: Int = 1,
    val currentXp: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null
)
package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteTechnique(
    val userId: String = "",
    val techniqueId: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)
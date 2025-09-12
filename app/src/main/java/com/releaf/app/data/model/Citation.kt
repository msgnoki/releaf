package com.releaf.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Citation(
    val citation: String,
    val auteur: String
)
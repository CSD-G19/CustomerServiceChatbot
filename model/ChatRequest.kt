package com.project.genassist_ecommerce.model

data class ChatRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)
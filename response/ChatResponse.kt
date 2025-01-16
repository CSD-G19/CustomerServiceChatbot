package com.project.genassist_ecommerce.response

data class ChatResponse(val choices: List<Choice>) {
    data class Choice(val text: String)
}

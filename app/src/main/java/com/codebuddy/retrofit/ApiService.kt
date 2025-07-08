package com.codebuddy.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Data class for sending the prompt
data class ChatRequest(val prompt: String)

// Data class for receiving the response from the backend
data class ChatResponse(val response: String)

interface ApiService {
    @POST("/chat")
    fun getChatResponse(@Body request: ChatRequest): Call<ChatResponse>
}

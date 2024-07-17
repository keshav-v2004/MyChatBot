package com.example.mychatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    val responses = mutableStateListOf<MessageModel>()

    private val _responseState = MutableStateFlow(responses)
    val responseState = _responseState.asStateFlow()


    val generativeModel = GenerativeModel(
        // The Gemini 1.5 models are versatile and work with most use cases
        modelName = "gemini-1.5-flash",
        // Access your API key as a Build Configuration variable (see "Set up your API key" above)
        apiKey = Constants.ApiKey
    )


    fun SendMessage(query: String) {

        viewModelScope.launch {
            try {
                _responseState.value.add(MessageModel(role = "user", query = query))

                _responseState.value.add(MessageModel(role = "model", query = "Typing..."))

                val chat = generativeModel.startChat(
                    history = responseState.value.map {
                        content(
                            role = it.role
                        ) {
                            text(it.query)
                        }
                    }
                )

                val response = chat.sendMessage(query)

                _responseState.value.removeLast()

                _responseState.value.add(
                    MessageModel(
                        role = "model",
                        query = response.text.toString()
                    )
                )
            } catch (e: Exception) {

                _responseState.value.removeLast()
                _responseState.value.add(
                    MessageModel(
                        role = "model",
                        query = "Something went wrong"
                    )
                )
            }

        }
    }

}
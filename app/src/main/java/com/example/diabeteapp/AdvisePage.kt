package com.example.diabeteapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

// Data classes for API communication
data class CohereRequest(
    val model: String = "command-r",
    val message: String,
    @SerializedName("chat_history")
    val chatHistory: List<ChatHistoryItem> = emptyList(),
    val temperature: Double = 0.7,
    @SerializedName("max_tokens")
    val maxTokens: Int = 300,
    val preamble: String? = null
)

data class ChatHistoryItem(
    val role: String,
    val message: String
)

data class CohereResponse(
    val text: String
)

// Retrofit interface for Cohere API
interface CohereApiService {
    @POST("v1/chat")
    suspend fun chat(
        @Header("Authorization") authorization: String,
        @Body request: CohereRequest
    ): CohereResponse
}

// UI Message data class
data class Message(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// ViewModel handling chat logic and API calls
class ChatViewModel : ViewModel() {
    // List of chat messages
    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    // Loading state for UI
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val apiService: CohereApiService
    private val apiKey = "Xk7aGwH5HnZiYhqpllckICwcqthZwnQlsLNemO8v"

    init {
        // Setup Retrofit for API calls
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.cohere.ai/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(CohereApiService::class.java)

        // Add welcome message when chat starts
        _messages.add(
            Message(
                "Hello! I'm your diabetes management assistant. I can help you with:\n\n• Blood sugar tracking\n• Meal planning tips\n• Exercise suggestions\n• General diabetes education\n\nHow can I help you today?",
                isUser = false
            )
        )
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.trim().isEmpty()) return

        // Add user message to chat
        _messages.add(Message(userMessage, isUser = true))

        // Make API call in background
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Build chat history for context (last 10 messages, excluding current user message)
                val chatHistory = mutableListOf<ChatHistoryItem>()
                val recentMessages = _messages.dropLast(1).takeLast(10)

                for (msg in recentMessages) {
                    chatHistory.add(
                        ChatHistoryItem(
                            role = if (msg.isUser) "USER" else "CHATBOT",
                            message = msg.text
                        )
                    )
                }

                // System prompt for diabetes assistant behavior
                val preamble = """You are a helpful diabetes management assistant. You can help users with:
- Blood sugar tracking and understanding readings
- Meal planning and carb counting tips
- Exercise recommendations
- Medication reminders
- General diabetes education and support

Important guidelines:
- Always remind users you're not a replacement for medical advice
- Encourage consulting healthcare providers for medical concerns
- Be supportive and encouraging
- Keep responses concise and helpful
- If asked about emergency symptoms, advise immediate medical attention"""

                // Create API request
                val request = CohereRequest(
                    message = userMessage,
                    chatHistory = chatHistory,
                    preamble = preamble
                )

                // Make API call
                val response = apiService.chat(
                    authorization = "Bearer $apiKey",
                    request = request
                )

                // Add bot response to chat
                val botMessage = response.text.ifEmpty { "Sorry, I couldn't generate a response." }
                _messages.add(Message(botMessage, isUser = false))

            } catch (e: retrofit2.HttpException) {
                // Handle HTTP errors (401 = invalid API key, etc.)
                _messages.add(
                    Message(
                        "API Error (${e.code()}): ${if (e.code() == 401) "Invalid API key" else "Server error"}",
                        isUser = false
                    )
                )
            } catch (e: java.net.UnknownHostException) {
                // Handle network connectivity issues
                _messages.add(
                    Message(
                        "Network error: Please check your internet connection",
                        isUser = false
                    )
                )
            } catch (e: Exception) {
                // Handle other unexpected errors
                _messages.add(
                    Message(
                        "Error: ${e.message ?: "Unknown error occurred"}",
                        isUser = false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Main chat screen composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvisePageScreen() {
    val viewModel = remember { ChatViewModel() }
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(viewModel.messages.size) {
        if (viewModel.messages.isNotEmpty()) {
            listState.animateScrollToItem(viewModel.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Diabetes Assistant",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Messages list
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Display all chat messages
            items(viewModel.messages) { message ->
                MessageBubble(message = message)
            }

            // Show loading indicator when waiting for AI response
            if (viewModel.isLoading.value) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Thinking...",
                            modifier = Modifier.padding(start = 8.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Input section with text field and send button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask about diabetes management...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                maxLines = 3
            )

            // Send button
            FloatingActionButton(
                onClick = {
                    if (textInput.trim().isNotEmpty()) {
                        viewModel.sendMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

// Individual message bubble with different styling for user vs bot messages
@Composable
fun MessageBubble(message: Message) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .padding(horizontal = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (message.isUser)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
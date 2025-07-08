package com.codebuddy.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.codebuddy.R
import com.codebuddy.offline.CacheManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ChatActivity : AppCompatActivity() {

    private lateinit var cacheManager: CacheManager
    private lateinit var backendUrl: String
    private lateinit var chatBox: TextView
    private lateinit var userInput: EditText
    private lateinit var sendBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        cacheManager = CacheManager(this)
        chatBox = findViewById(R.id.chatBox)
        userInput = findViewById(R.id.userInput)
        sendBtn = findViewById(R.id.sendButton)

        val username = intent.getStringExtra("username") ?: "User"
        backendUrl = intent.getStringExtra("backendUrl") ?: "http://localhost:8000"

        appendChat("Welcome $username 👋\n")

        sendBtn.setOnClickListener {
            val message = userInput.text.toString()
            if (message.isNotBlank()) {
                appendChat("👤 $message\n")
                userInput.setText("")

                val cached = cacheManager.getCachedReply(message)
                if (cached != null) {
                    appendChat("🤖 (Cached) $cached\n")
                } else {
                    sendToBackend(message)
                }
            }
        }
    }

    private fun appendChat(text: String) {
        runOnUiThread {
            chatBox.append(text)
        }
    }

    private fun sendToBackend(message: String) {
        val client = OkHttpClient()
        val json = JSONObject().put("message", message)
        val body = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("$backendUrl/chat")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                appendChat("❌ Failed to connect: ${e.message}\n")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val resBody = response.body?.string()
                    val jsonRes = JSONObject(resBody)
                    val reply = jsonRes.optString("response", "🤖 (No reply)")
                    cacheManager.saveReply(message, reply)
                    appendChat("🤖 $reply\n")
                } else {
                    appendChat("❌ Error: ${response.code}\n")
                }
            }
        })
    }
}

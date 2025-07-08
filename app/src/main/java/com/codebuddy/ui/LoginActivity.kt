// 🔹 LoginActivity.kt
package com.codebuddy.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.codebuddy.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.usernameInput)
        val backendUrlInput = findViewById<EditText>(R.id.backendUrlInput)
        val loginButton = findViewById<Button>(R.id.loginButton)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        backendUrlInput.setText(prefs.getString("backend_url", ""))

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val backendUrl = backendUrlInput.text.toString()

            // Save backend URL
            prefs.edit().putString("backend_url", backendUrl).apply()

            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("backendUrl", backendUrl)
            startActivity(intent)
        }
    }
}

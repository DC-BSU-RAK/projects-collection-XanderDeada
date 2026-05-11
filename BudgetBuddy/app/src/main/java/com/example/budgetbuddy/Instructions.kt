package com.example.budgetbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Instructions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_instructions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvInstructions = findViewById<TextView>(R.id.tv_instructions_text)
        val btnBack = findViewById<Button>(R.id.btn_back_instructions)
        val instructionText = """
            Welcome to Budget Buddy!
            
            1. SETUP: Go to Settings to set your Name, Income, and Daily Limit.
            
            2. LOGGING: Use the 'Add' button to log expenses. These will subtract from your balance and daily limit automatically.
            
            3. TRACKING: Your Dashboard shows real-time progress bars. If you exceed your daily limit, the text will turn RED.
            
            4. MANAGING HISTORY: 
               • Tap 'History' to see all logged expenses.
               • LONG-PRESS (Hold) any transaction to remove it.
               • Deleting a transaction will automatically REFUND that amount back to your monthly and daily totals.
            
            5. RESET: You can wipe all data in the Settings menu if you want to start fresh.
        """.trimIndent()

        tvInstructions.text = instructionText

        btnBack.setOnClickListener { finish() }
    }
}
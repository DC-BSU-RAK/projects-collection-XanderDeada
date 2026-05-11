package com.example.budgetbuddy

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddExpense : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_expense)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etAmount = findViewById<EditText>(R.id.et_amount)
        val etCategory = findViewById<EditText>(R.id.et_category)
        val btnLog = findViewById<Button>(R.id.btn_log_expense)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_expense)
        val sharedPref = getSharedPreferences("BudgetBuddyPrefs", Context.MODE_PRIVATE)

        btnLog.setOnClickListener {
            val amountStr = etAmount.text.toString()
            val category = etCategory.text.toString()

            if (amountStr.isNotEmpty() && category.isNotEmpty()) {
                val amount = amountStr.toFloat()
                val totalMonthly = sharedPref.getFloat("total_monthly_spent", 0f) + amount
                val totalDaily = sharedPref.getFloat("spent_today", 0f) + amount
                val currentHistory = sharedPref.getStringSet("history_list", mutableSetOf())?.toMutableSet()
                currentHistory?.add("$category: -$${amountStr}")
                val editor = sharedPref.edit()
                editor.putFloat("total_monthly_spent", totalMonthly)
                editor.putFloat("spent_today", totalDaily)
                editor.putStringSet("history_list", currentHistory)
                editor.apply()

                Toast.makeText(this, "Logged $category!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Please enter details", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener { finish() }
    }
}
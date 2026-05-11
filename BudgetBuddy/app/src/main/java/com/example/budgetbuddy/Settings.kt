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

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etName = findViewById<EditText>(R.id.et_name)
        val etIncome = findViewById<EditText>(R.id.et_income)
        val etBudget = findViewById<EditText>(R.id.et_budget)
        val etDailyLimit = findViewById<EditText>(R.id.et_daily_limit)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnClear = findViewById<Button>(R.id.btn_clear)
        val sharedPref = getSharedPreferences("BudgetBuddyPrefs", Context.MODE_PRIVATE)

        etName.setText(sharedPref.getString("user_name", ""))
        etIncome.setText(sharedPref.getFloat("monthly_income", 0f).toString())
        etBudget.setText(sharedPref.getFloat("monthly_budget", 0f).toString())
        etDailyLimit.setText(sharedPref.getFloat("daily_limit", 150f).toString())

        btnSave.setOnClickListener {
            val name = etName.text.toString()
            val income = etIncome.text.toString()
            val budget = etBudget.text.toString()
            val daily = etDailyLimit.text.toString()

            if (name.isNotEmpty() && income.isNotEmpty() && budget.isNotEmpty() && daily.isNotEmpty()) {
                val editor = sharedPref.edit()
                editor.putString("user_name", name)
                editor.putFloat("monthly_income", income.toFloat())
                editor.putFloat("monthly_budget", budget.toFloat())
                editor.putFloat("daily_limit", daily.toFloat())
                editor.apply()

                Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill in all fields, Buddy!", Toast.LENGTH_SHORT).show()
            }
        }

        btnClear.setOnClickListener {
            sharedPref.edit().clear().apply()
            Toast.makeText(this, "All Data Reset!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
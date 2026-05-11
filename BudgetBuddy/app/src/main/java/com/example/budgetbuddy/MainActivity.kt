package com.example.budgetbuddy

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<ImageButton>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        findViewById<ImageButton>(R.id.btn_manage_daily).setOnClickListener {
            startActivity(Intent(this, Settings::class.java))
        }
        findViewById<ImageButton>(R.id.btn_help).setOnClickListener {
            startActivity(Intent(this, Instructions::class.java))
        }
        findViewById<Button>(R.id.nav_add).setOnClickListener {
            startActivity(Intent(this, AddExpense::class.java))
        }
        findViewById<Button>(R.id.nav_history).setOnClickListener {
            startActivity(Intent(this, History::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("BudgetBuddyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val todayDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val lastSavedDate = sharedPref.getString("last_date", "")
        if (todayDate != lastSavedDate) {
            editor.putFloat("spent_today", 0f)
            editor.putString("last_date", todayDate)
            editor.apply()
        }

        val name = sharedPref.getString("user_name", "Buddy")
        val income = sharedPref.getFloat("monthly_income", 0f)
        val monthlyGoal = sharedPref.getFloat("monthly_budget", 0f)
        val totalSpent = sharedPref.getFloat("total_monthly_spent", 0f)
        val dailyLimit = sharedPref.getFloat("daily_limit", 150f)
        val spentToday = sharedPref.getFloat("spent_today", 0f)

        findViewById<TextView>(R.id.tv_welcome).text = "Welcome, $name!"
        findViewById<TextView>(R.id.tv_balance).text = "$${String.format("%.2f", income - totalSpent)}"
        findViewById<TextView>(R.id.tv_income_val).text = "$${String.format("%.2f", income)}"
        findViewById<TextView>(R.id.tv_expenses_val).text = "$${String.format("%.2f", totalSpent)}"

        val tvOverview = findViewById<TextView>(R.id.tv_overview_ratio)
        val pbMonthly = findViewById<ProgressBar>(R.id.pb_monthly)
        tvOverview.text = "${totalSpent.toInt()}/${monthlyGoal.toInt()}"
        pbMonthly.max = if (monthlyGoal > 0) monthlyGoal.toInt() else 100
        pbMonthly.progress = totalSpent.toInt()
        val tvDaily = findViewById<TextView>(R.id.tv_daily_ratio)
        val pbDaily = findViewById<ProgressBar>(R.id.pb_daily)
        tvDaily.text = "${spentToday.toInt()}/${dailyLimit.toInt()}"
        pbDaily.max = if (dailyLimit > 0) dailyLimit.toInt() else 100
        pbDaily.progress = spentToday.toInt()

        if (spentToday > dailyLimit) {
            tvDaily.setTextColor(Color.RED)
        } else {
            tvDaily.setTextColor(Color.WHITE)
        }
    }
}
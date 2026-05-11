package com.example.budgetbuddy

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }

        loadHistory()
    }

    private fun loadHistory() {
        val container = findViewById<LinearLayout>(R.id.history_container)
        val sharedPref = getSharedPreferences("BudgetBuddyPrefs", Context.MODE_PRIVATE)
        val historySet = sharedPref.getStringSet("history_list", mutableSetOf())
        val historyList = historySet?.toMutableList() ?: mutableListOf()

        container.removeAllViews()

        if (historyList.isEmpty()) {
            val emptyTv = TextView(this)
            emptyTv.text = "No transactions logged yet."
            emptyTv.setTextColor(resources.getColor(R.color.white))
            container.addView(emptyTv)
            return
        }

        for (transaction in historyList) {
            val tv = TextView(this)
            tv.text = transaction
            tv.setTextColor(resources.getColor(R.color.white))
            tv.textSize = 18f
            tv.setPadding(20, 40, 20, 40)
            tv.minHeight = 48
            tv.setBackgroundResource(android.R.drawable.list_selector_background)
            tv.setOnLongClickListener {
                showDeleteDialog(transaction, historyList)
                true
            }

            container.addView(tv)
        }
    }

    private fun showDeleteDialog(transaction: String, historyList: MutableList<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Transaction?")
        builder.setMessage("Do you want to remove '$transaction' and refund the amount?")

        builder.setPositiveButton("Delete") { _, _ ->
            deleteTransaction(transaction, historyList)
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun deleteTransaction(transaction: String, historyList: MutableList<String>) {
        val sharedPref = getSharedPreferences("BudgetBuddyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val amountMatch = Regex("""\$(\d+\.?\d*)""").find(transaction)
        val amount = amountMatch?.groupValues?.get(1)?.toFloat() ?: 0f
        val currentMonthly = sharedPref.getFloat("total_monthly_spent", 0f)
        val currentDaily = sharedPref.getFloat("spent_today", 0f)

        editor.putFloat("total_monthly_spent", (currentMonthly - amount).coerceAtLeast(0f))
        editor.putFloat("spent_today", (currentDaily - amount).coerceAtLeast(0f))
        historyList.remove(transaction)
        editor.putStringSet("history_list", historyList.toSet())

        editor.apply()

        Toast.makeText(this, "Transaction Removed & Refunded", Toast.LENGTH_SHORT).show()
        loadHistory()
    }
}
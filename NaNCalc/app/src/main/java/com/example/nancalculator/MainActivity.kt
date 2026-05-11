package com.example.nancalculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var choice1: Int? = null
    private var choice2: Int? = null

    private val discoveredReactions = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val s1 = findViewById<ImageView>(R.id.slot1)
        val s2 = findViewById<ImageView>(R.id.slot2)
        val sumBtn = findViewById<Button>(R.id.btn_sum)
        val collectionBtn = findViewById<Button>(R.id.btn_collection)
        val resetBtn = findViewById<Button>(R.id.btn_reset)
        val helpBtn = findViewById<ImageButton>(R.id.btn_help)
        helpBtn.setOnClickListener {
            val intent = Intent(this, InstructionsTab::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.btn_pyro).setOnClickListener { handleInput(R.drawable.pyro_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_hydro).setOnClickListener { handleInput(R.drawable.hydro_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_anemo).setOnClickListener { handleInput(R.drawable.anemo_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_cryo).setOnClickListener { handleInput(R.drawable.cryo_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_electro).setOnClickListener { handleInput(R.drawable.electro_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_geo).setOnClickListener { handleInput(R.drawable.geo_icon, s1, s2) }
        findViewById<ImageButton>(R.id.btn_dendro).setOnClickListener { handleInput(R.drawable.dendro_icon, s1, s2) }

        sumBtn.setOnClickListener {
            if (choice1 != null && choice2 != null) {
                val reactionName = getReactionName(choice1!!, choice2!!)

                if (reactionName != "NaN") {
                    val element1Name = getElementName(choice1!!)
                    val element2Name = getElementName(choice2!!)
                    val fullFormula = "$element1Name + $element2Name = $reactionName"

                    discoveredReactions.add(fullFormula)

                    Toast.makeText(this, "Discovered: $reactionName!", Toast.LENGTH_SHORT).show()

                    collectionBtn.text = getString(R.string.view_reactions, discoveredReactions.size)
                    collectionBtn.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this, "No Reaction", Toast.LENGTH_SHORT).show()
                }
            }
        }

        collectionBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Elemental Codex")

            val message = if (discoveredReactions.isEmpty()) "No recipes found yet."
            else discoveredReactions.sorted().joinToString("\n• ", prefix = "• ")

            builder.setMessage(message)
            builder.setPositiveButton("Close") { dialog, _ -> dialog.dismiss() }
            builder.show()
        }

        resetBtn.setOnClickListener {
            choice1 = null
            choice2 = null
            s1.setImageResource(0)
            s2.setImageResource(0)
        }
    }

    private fun handleInput(drawableId: Int, s1: ImageView, s2: ImageView) {
        if (choice1 == null) {
            choice1 = drawableId
            s1.setImageResource(drawableId)
        } else if (choice2 == null) {
            choice2 = drawableId
            s2.setImageResource(drawableId)
        }
    }

    private fun getElementName(id: Int): String {
        return when (id) {
            R.drawable.pyro_icon -> "Pyro"
            R.drawable.hydro_icon -> "Hydro"
            R.drawable.anemo_icon -> "Anemo"
            R.drawable.cryo_icon -> "Cryo"
            R.drawable.electro_icon -> "Electro"
            R.drawable.geo_icon -> "Geo"
            R.drawable.dendro_icon -> "Dendro"
            else -> "Unknown"
        }
    }

    private fun getReactionName(e1: Int, e2: Int): String {
        val combo = setOf(e1, e2)
        return when {
            combo.contains(R.drawable.pyro_icon) && combo.contains(R.drawable.hydro_icon) -> "Vaporize"
            combo.contains(R.drawable.pyro_icon) && combo.contains(R.drawable.electro_icon) -> "Overload"
            combo.contains(R.drawable.pyro_icon) && combo.contains(R.drawable.cryo_icon) -> "Melt"
            combo.contains(R.drawable.hydro_icon) && combo.contains(R.drawable.cryo_icon) -> "Frozen"
            combo.contains(R.drawable.hydro_icon) && combo.contains(R.drawable.electro_icon) -> "Electro-Charged"
            combo.contains(R.drawable.hydro_icon) && combo.contains(R.drawable.dendro_icon) -> "Bloom"
            combo.contains(R.drawable.electro_icon) && combo.contains(R.drawable.dendro_icon) -> "Quicken"
            combo.contains(R.drawable.pyro_icon) && combo.contains(R.drawable.dendro_icon) -> "Burning"
            else -> "NaN"
        }
    }
}
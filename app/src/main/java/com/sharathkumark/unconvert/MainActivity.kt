package com.sharathkumark.unconvert

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


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

        val etWeightKgs = findViewById<EditText>(R.id.etWeightKgs)
        val tvWeightPounds = findViewById<TextView>(R.id.tvWeightPounds)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)

        btnCalculate.setOnClickListener {
            var weight = etWeightKgs.text.toString().toDoubleOrNull()
            weight = weight?.times(2.205)
            if (weight != null) {
                tvWeightPounds.text = "Equivalent weight in pounds is " + weight.toString()
            }
            else {
                tvWeightPounds.text = ""
                Toast.makeText(this, "Enter a valid value of weight!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
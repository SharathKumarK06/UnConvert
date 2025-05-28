package com.sharathkumark.unconvert

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.time.times


class MainActivity : AppCompatActivity() {
    val TAG: String = "UNCONVERT"

    private val dimensionsTable = mapOf(
        "Length" to mapOf(
            "Kilometre" to 1.0,
            "Metre" to 1000.0,
            "Centimetre" to 100000.0,
        ),
        "Time" to mapOf(
            "Second" to 3600.0,
            "Minute" to 60.0,
            "Hour" to 1.0,
        ),
        "Mass" to mapOf(
            "Tonne" to 1.0,
            "Kilogram" to 1000.0,
            "Gram" to 1000000.0,
        ),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create spinnerDimensions
        val spinnerDimensions: Spinner = findViewById(R.id.spinnerDimensions)
        // Spinner fo from and to
        val spinnerFrom: Spinner = findViewById(R.id.spinnerFrom)
        val spinnerTo: Spinner = findViewById(R.id.spinnerTo)
        // ArrayAdapter for data manipulation dynamically
        lateinit var spinnerFromAdapter: ArrayAdapter<String>
        lateinit var spinnerToAdapter: ArrayAdapter<String>
        // Units list for both form and to spinners to access
//        var units: MutableList<String> = mutableListOf()
        var previousFromUnit: Int = -1
        var previousToUnit: Int = -1

        // Textview for error message
        val tvError = findViewById<TextView>(R.id.tvError)

        // From and to edittext
        val etFrom = findViewById<EditText>(R.id.etFrom)
        val etTo = findViewById<EditText>(R.id.etTo)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        var fromValue: Double? = 0.0
        var toValue: Double? = 0.0

        // user's choices for calculation
        var unit_sets = mutableMapOf<String, String>()

        // Create an ArrayAdapter using the string array and a default spinnerDimensions layout.
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dimensionsTable.keys.toList()
        ).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDimensions.adapter = adapter
        }

        // onItemSelectedListener for spinnerDimensions
        spinnerDimensions.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                // get dimensions
                val dimensions = dimensionsTable[parent?.getItemAtPosition(pos)]!!

                // to store the user's choices of spinners for calculation
                unit_sets["dimensions"] = parent?.getItemAtPosition(pos).toString()

                // to make units list of specific dimension available for spinnerFrom and spinnerTo
//                units = dimensions.keys.toMutableList()

                // ArrayAdapter for spinnerFrom
                spinnerFromAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    dimensions.keys.toMutableList()
                )
                spinnerFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = spinnerFromAdapter
                // Set default selection to next item
                previousFromUnit = 0
                spinnerFrom.setSelection(previousFromUnit)

                // ArrayAdapter for spinnerTo
                spinnerToAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    dimensions.keys.toMutableList()
                )
                spinnerToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTo.adapter = spinnerToAdapter
                // Set default selection to next item
                previousToUnit = 1
                spinnerTo.setSelection(previousToUnit)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        spinnerFrom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val unit = parent?.selectedItem.toString()
                // If currently selected unit in "From" same as "To", set "To" unit as previous
                // "From" unit
                if (unit == spinnerTo.selectedItem.toString() && previousFromUnit != -1) {
                    spinnerTo.setSelection(previousFromUnit)
                }
                previousFromUnit = pos
                // Set "From" unit to unit_sets for recording user selection for calculateion
                unit_sets["from"] = unit
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        // onItemSelectedListener for spinnerTo
        spinnerTo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val unit = parent?.selectedItem.toString()
                // If currently selected unit in "To" same as "From", set "From" unit as previous
                // "To" unit
                if (unit == spinnerFrom.selectedItem.toString() && previousToUnit != -1) {
                    spinnerFrom.setSelection(previousToUnit)
                }
                previousToUnit = pos
                // Set "To" unit to unit_sets for recording user selection for calculateion
                unit_sets["to"] = unit
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        btnCalculate.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View?) {
                if (etFrom.text.isNullOrEmpty()) {
                    // Get values of from
                    tvError.setText(R.string.error_no_input)
                    return
                }
                if (etFrom.text.toString().toDoubleOrNull() == null) {
                    tvError.setText(R.string.error_invalid_input)
                    return
                }

                fromValue = etFrom.text.toString().toDouble()
                toValue = calculateEquivalent(1.0, unit_sets)
                etTo.setText(toValue.toString())
            }
        })
    }

    private fun calculateEquivalent(from: Double, unit_sets: MutableMap<String, String>): Double {
//        Log.v(TAG, "Input: ${unit_sets}")
//        Log.v(TAG, "Dimensions: ${dimensionsTable[unit_sets["dimensions"]]}")
//        Log.v(TAG, "${}")
        val fromFactor = dimensionsTable[unit_sets["dimensions"]]!![unit_sets["from"]]!!
        val toFactor = dimensionsTable[unit_sets["dimensions"]]!![unit_sets["to"]]!!
        val to = (from * toFactor)/fromFactor
        return to
    }
}
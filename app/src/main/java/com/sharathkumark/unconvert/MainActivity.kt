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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {
    private val dimensionsTable = mapOf(
        "Length" to mapOf(
            "Kilometre" to 1.0,
            "Metre" to 1000.0,
            "Centimetre" to 100000.0,
            "Millimetre" to 1e+6,
            "Micrometre" to 1e+9,
            "Nanometre" to 1e+12,
            "Mile" to 0.6214,
            "Yard" to 1093.6100,
            "Foot" to 3280.8400,
            "Inch" to 39370.1000,
            "Nautical mile" to 0.5400,
        ),
        "Time" to mapOf(
            "Nanosecond" to 3.154e+18,
            "Microsecond" to 3.154e+15,
            "Millisecond" to 3.154e+12,
            "Second" to 3.154e+9,
            "Minute" to 5.256e+7,
            "Hour" to 876000,
            "Day" to 36500,
            "Week" to 5214.29,
            "Month" to 1199.9997,
            "Calendar year" to 100.0,
            "Decade" to 10.0,
            "Century" to 1.0,
        ),
        "Mass" to mapOf(
            "Tonne" to 1.0,
            "Kilogram" to 1000.0,
            "Gram" to 1e+6,
            "Milligram" to 1e+9,
            "Microgram" to 1e+12,
            "Imperial ton" to 0.9842,
            "US ton" to 1.1023,
            "Stone" to 157.4729,
            "Stone" to 157.4729,
            "Pound" to 2204.6200,
            "Ounce" to 35273.9200,
        ),
        "Speed" to mapOf(
            "Metre per second" to 1.0,
            "Kilometre per second" to 3.6,
            "Mile per hour" to 2.2370,
            "Foot per hour" to 3.2808,
            "Knot" to 1.9438,
        )
    )
    val x = 1e+6

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
        var previousFromUnit: Int = -1
        var previousToUnit: Int = -1

        // Textview for error message
        val tvError = findViewById<TextView>(R.id.tvError)

        // From and to edittext
        val etFrom = findViewById<EditText>(R.id.etFrom)
        val etTo = findViewById<EditText>(R.id.etTo)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        var fromValue: Double?
        var toValue: Double?

        // user's choices for calculation
        val inputUnitSet = mutableMapOf<String, String>()

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
                inputUnitSet["dimensions"] = parent?.getItemAtPosition(pos).toString()

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
                // Set "From" unit to inputUnitSet for recording user selection for calculation
                inputUnitSet["from"] = unit
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
                // Set "To" unit to inputUnitSet for recording user selection for calculation
                inputUnitSet["to"] = unit
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

                // Clear error text
                tvError.text = null

                fromValue = etFrom.text.toString().toDouble()
                toValue = calculateEquivalent(fromValue!!, inputUnitSet)
                etTo.setText(toValue.toString())
            }
        })
    }

    private fun calculateEquivalent(from: Double, inputUnitSet: MutableMap<String, String>): Double {
        val fromFactor: Double = dimensionsTable[inputUnitSet["dimensions"]]!![inputUnitSet["from"]] as Double
        val toFactor: Double = dimensionsTable[inputUnitSet["dimensions"]]!![inputUnitSet["to"]] as Double
        val to = (from * toFactor)/fromFactor
        return to
    }
}
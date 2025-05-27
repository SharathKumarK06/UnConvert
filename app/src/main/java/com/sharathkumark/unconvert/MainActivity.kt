package com.sharathkumark.unconvert

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import androidx.core.view.size


class MainActivity : AppCompatActivity() {
    val TAG: String = "UNCONVERT"

    private val dimensions_table = mapOf(
        "Length" to mapOf(
            "Kilometre" to 1,
            "Metre" to 1000,
            "Centimetre" to 100000,
        ),
        "Time" to mapOf(
            "Second" to 3600,
            "Minute" to 60,
            "Hour" to 1,
        ),
        "Mass" to mapOf(
            "Tonne" to 1,
            "Kilogram" to 1000,
            "Gram" to 1000000,
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
        val spinnerFrom: Spinner = findViewById(R.id.spinnerFrom)
        val spinnerTo: Spinner = findViewById(R.id.spinnerTo)
        lateinit var spinnerFromAdapter: ArrayAdapter<String>
        lateinit var spinnerToAdapter: ArrayAdapter<String>
        lateinit var units: MutableList<String>

        // Create an ArrayAdapter using the string array and a default spinnerDimensions layout.
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dimensions_table.keys.toList()
        ).also {
            adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDimensions.adapter = adapter
        }

        // TODO("store default unit and spinner id as a pair in unit so other spinner can see")

        // onItemSelectedListener for spinnerDimensions
        spinnerDimensions.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                // get dimensions
                val dimensions = dimensions_table[parent?.getItemAtPosition(pos)]!!
                units = dimensions.keys.toMutableList()

                // ArrayAdapter for spinnerFrom
                spinnerFromAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                )
                spinnerFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = spinnerFromAdapter

                // ArrayAdapter for spinnerTo
                spinnerToAdapter = ArrayAdapter(
                    applicationContext,
                    android.R.layout.simple_spinner_item,
                    dimensions.keys.toMutableList()
                )
                spinnerToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTo.adapter = spinnerToAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        // onItemSelectedListener for spinnerFrom
        spinnerFrom.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                // Clear list and re add all units
                spinnerToAdapter.clear()
                spinnerToAdapter.addAll(units)

                val unit = parent?.selectedItem
                spinnerToAdapter.remove(unit.toString())

                for (i in 0..<spinnerToAdapter.count) {
                    Log.v(TAG, "Inside spinnerFrom From, Items: ${spinnerFromAdapter.getItem(i)}")
                    Log.v(TAG, "Inside spinnerFrom To, Items: ${spinnerToAdapter.getItem(i)}")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

        // onItemSelectedListener for spinnerTo
        spinnerTo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                // Clear list and re add all units
                spinnerFromAdapter.clear()
                spinnerFromAdapter.addAll(units)

                val unit = parent?.selectedItem
                spinnerFromAdapter.remove(unit.toString())

                for (i in 0..<spinnerFromAdapter.count) {
                    Log.v(TAG, "Inside spinnerTo To, Items: ${spinnerToAdapter.getItem(i)}")
                    Log.v(TAG, "Inside spinnerTo From, Items: ${spinnerFromAdapter.getItem(i)}")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

    }

//    private fun readjustList(parent: AdapterView<*>?, pos: Int): String {
//        when (parent?.id) {
//            R.id.spinnerFrom -> {
//                //
//                val item = parent.selectedItem
//            }
//            R.id.spinnerTo -> {
//                //
//            }
//        }
//    }
}
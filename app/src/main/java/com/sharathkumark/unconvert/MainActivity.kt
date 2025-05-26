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


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var spinnerFrom: Spinner
    private lateinit var spinnerTo: Spinner
    private val all_units = mapOf(
        "Length" to listOf(
            "Kilometre",
            "Metre",
            "Centimetre",
        ),
        "Time" to listOf(
            "Second",
            "Minute",
            "Hour",
        ),
        "Mass" to listOf(
            "Tonne",
            "Kilogram",
            "Gram",
        ),
    )

    val TAG: String = "UNCONVERT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create spinnerQuantity
        val spinnerQuantity: Spinner = findViewById(R.id.spinnerQuantity)
        spinnerFrom = findViewById(R.id.spinnerFrom)
        spinnerTo = findViewById(R.id.spinnerTo)

        // Create an ArrayAdapter using the string array and a default spinnerQuantity layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.spinnerQuantity,
            android.R.layout.simple_spinner_item
        ).also {
            adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinnerQuantity.
            spinnerQuantity.adapter = adapter
        }

        // specify the interface implementation of onItemSelectListener
        spinnerQuantity.onItemSelectedListener = this
    }

    private fun returnSubList(mylist: List<String>, index: Int): List<String> {
        return mylist.filter {
            it != mylist[index]
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        // An item is selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos).
        val units = all_units[parent.getItemAtPosition(pos)]!!.toList()
        var unitsFrom = returnSubList(units, 0)

        // create adapter for spinnerfrom
        ArrayAdapter(this, android.R.layout.simple_spinner_item, unitsFrom).also {
            adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerFrom.adapter = adapter
        }
        var unitsTo = returnSubList(units, 1)

        // create adapter for spinnerto
        ArrayAdapter(this, android.R.layout.simple_spinner_item, unitsTo).also {
            adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerTo.adapter = adapter
        }

//        Log.v(TAG, "unitsFrom")
//        for (item in unitsTo) {
//            Log.v(TAG, "Item: $item")
//        }
//
//        Log.v(TAG, "unitsTo")
//        for (item in unitsTo) {
//            Log.v(TAG, "Item: $item")
//        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        //
    }
}
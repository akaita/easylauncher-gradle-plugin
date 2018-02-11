package com.akaita.android.easylauncher.example

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Hello, Android!")
                .setMessage("This is an example app.")
                .setPositiveButton("OK", null)
                .show()
        }
    }
}

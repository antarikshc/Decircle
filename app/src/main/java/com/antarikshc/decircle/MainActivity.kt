package com.antarikshc.decircle

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edit_slider_value.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE) {
                animate()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun animate(v: View? = null) {
        slider.setProgress(edit_slider_value.text.toString().toInt())
    }
}
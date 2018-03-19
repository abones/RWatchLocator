package com.rubius.rwatchlocator

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonReset.setOnClickListener {
            locatorView.reset()
        }
        locatorView.listener = {
            scale, translationX, translationY ->
            label.text = "$scale, $translationX, $translationY"
        }
    }
}

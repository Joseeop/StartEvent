package com.example.startevent

import android.widget.LinearLayout

object Utility {
    fun setHeightLinearLayout(ly: LinearLayout, value: Int){
        val params: LinearLayout.LayoutParams = ly.layoutParams as LinearLayout.LayoutParams
        params.height = value
        ly.layoutParams = params
    }
}
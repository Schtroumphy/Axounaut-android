package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.view.View
import java.text.DecimalFormat


fun Double.formatDouble() : String{
    return DecimalFormat("#.##").format(this.toFloat())
}

fun View.onClick() = this.setOnClickListener {

}

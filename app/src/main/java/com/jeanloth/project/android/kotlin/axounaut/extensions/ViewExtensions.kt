package com.jeanloth.project.android.kotlin.axounaut.extensions

import java.text.DecimalFormat


fun Double.formatDouble() : String{
    return DecimalFormat("#.##").format(this.toFloat())
}

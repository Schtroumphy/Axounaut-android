package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.util.Log
import androidx.compose.ui.text.capitalize
import java.util.*

fun String.isPhoneValid() : Boolean{
    try{
        this.toInt()
        return this.length == 10
    } catch (e : NumberFormatException){
        Log.d("[String extensions]", "String $this cannot be cast to double.")
        return false
    }
}

fun String.toCamelCase() : String{
    val test = this.split(" ").map { it.capitalize() }
    return test.joinToString().replace(",", "")
}

fun String.capitalize(): String{
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

fun displayPreparingTime(time : Float?) : String{
    val time = time ?: 0f
    if(time == 0f) return ""
    val hour = time.toInt()
    if(hour == 0) return "30min"
    val minutes = time % hour
    return if(minutes != 0f) "$hour h 30" else "$hour h"
}

package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.util.Log
import androidx.compose.ui.text.capitalize

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

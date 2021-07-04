package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.util.Log

fun String.isPhoneValid() : Boolean{
    try{
        this.toInt()
        return this.length == 10
    } catch (e : NumberFormatException){
        Log.d("[String extensions]", "String $this cannot be cast to double.")
        return false
    }
}

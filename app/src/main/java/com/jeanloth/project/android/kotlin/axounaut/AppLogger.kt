package com.jeanloth.project.android.kotlin.axounaut

import android.util.Log
import java.lang.StringBuilder

object AppLogger {

    fun <T> Class<T>.logD(vararg message : String){
        val sb = StringBuilder().apply {
            message.forEachIndexed { index, message ->
                if(index == 0) append("\n ")
                append(message + "\n")
            }
        }
        Log.d("[${this.simpleName}]", sb.toString())
    }

    fun <T> Class<T>.logE(message : String){
        Log.e("[${this.simpleName}]", message)
    }

    fun <T> Class<T>.logI(message : String){
        Log.i("[${this.simpleName}]", message)
    }
}
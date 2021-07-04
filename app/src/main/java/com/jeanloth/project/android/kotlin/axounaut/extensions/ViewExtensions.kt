package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import java.text.DecimalFormat


fun Double.formatDouble() : String{
    return DecimalFormat("#.##").format(this.toFloat())
}

fun View.onClick() = this.setOnClickListener {

}

fun View.openPopUpMenu(context: Context, menu : Int, map : Map<Int, (() -> Unit)>) {
    this.setOnClickListener {
        //Creating the instance of PopupMenu
        val popup = PopupMenu( context, this)

        //Inflating the Popup using xml file
        popup.menuInflater.inflate(menu, popup.menu)

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            for((key, value) in map){
                when (item.itemId) {
                    key -> value
                    else -> {}
                }
            }
            true
        }
        popup.show() //showing popup menu
    }
}

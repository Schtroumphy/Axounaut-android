package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeanloth.project.android.kotlin.axounaut.R
import splitties.alertdialog.appcompat.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.textColorResource
import java.text.DecimalFormat


fun Double.formatDouble() : String{
    return DecimalFormat("#.##").format(this.toFloat())
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

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun BottomSheetDialog.fullScreen() : BottomSheetDialog {
    behavior.apply {
        isFitToContents = true
        skipCollapsed = true
        state = BottomSheetBehavior.STATE_EXPANDED
        isHideable = false
        isDraggable = false

        addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                when (state) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        dismiss()
                    }
                    else -> {
                    }
                }
            }

            override fun onSlide(view: View, slideOffest: Float) {

            }
        })
    }
    return this
}

fun displayDialog(
    context : Context, titleRef : Int, contentRef : Int, positiveButtonLabelRef : Int, positiveAction : (() -> Unit),
    negativeButtonLabelRef : Int, negativeAction : (() -> Unit), positiveButtonColor : Int = R.color.see_green, negativeButtonColor : Int = R.color.gray_2
){
    context.materialAlertDialog {
            title = context.getString(titleRef)
            message = context.getString(contentRef)
            positiveButton(positiveButtonLabelRef) {
                positiveAction.invoke()
                it.dismiss()
            }
            negativeButton(negativeButtonLabelRef) {
                negativeAction.invoke()
                it.dismiss()
            }
        }.onShow {
            positiveButton.textColorResource = positiveButtonColor
            negativeButton.textColorResource = negativeButtonColor
        }.show()
}



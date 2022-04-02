package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jeanloth.project.android.kotlin.axounaut.R
import splitties.alertdialog.appcompat.*
import splitties.alertdialog.material.materialAlertDialog
import splitties.views.textColorResource
import java.text.DecimalFormat


fun Double.formatDouble(): String {
    return DecimalFormat("#.##").format(this.toFloat())
}

fun View.openPopUpMenu(context: Context, menu: Int, map: Map<Int, (() -> Unit)>) {
    this.setOnClickListener {
        //Creating the instance of PopupMenu
        val popup = PopupMenu(context, this)

        //Inflating the Popup using xml file
        popup.menuInflater.inflate(menu, popup.menu)

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener { item ->
            for ((key, value) in map) {
                when (item.itemId) {
                    key -> value
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


fun BottomSheetDialog.fullScreen(): BottomSheetDialog {
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
    context: Context,
    titleRef: Int,
    contentRef: Int? = null,
    contentMessage: String?= null,
    positiveButtonLabelRef: Int? = null,
    positiveAction: (() -> Unit)? = null,
    negativeButtonLabelRef: Int,
    negativeAction: (() -> Unit),
    positiveButtonColor: Int = R.color.see_green,
    negativeButtonColor: Int = R.color.gray_2
) {
    context.materialAlertDialog {
        title = context.getString(titleRef)
        message = if(contentRef != null) context.getString(contentRef) else contentMessage ?: ""
        if (positiveButtonLabelRef != null && positiveAction != null) {
            positiveButton(positiveButtonLabelRef) {
                positiveAction.invoke()
                it.dismiss()
            }
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

/** Interface for actions when keyboard appears and disappears **/
fun interface OnKeyBoardVisibilityListener{
    fun onVisibilityChanged(visible: Boolean)
}

/**
 * Add a keyboard listener on activity screen for the fragment
 * Remove the listener when fragment is destroyed
 */
fun Fragment.setKeyBoardVisibilityListener(listener: OnKeyBoardVisibilityListener) {
    val parentView = requireActivity().findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
    if(parentView == null) {
        Log.e("TAg", "Could not get parent view for keyboard visibility listener for ${javaClass.simpleName}")
        return
    }
    val globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
        var alreadyOpen: Boolean = false
        val defaultKeyboardHeightDP = 100
        val estimatedKeyboardDP =
            defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
        val rect = Rect()
        override fun onGlobalLayout() {
            val estimatedKeyboardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                estimatedKeyboardDP.toFloat(),
                parentView.resources.displayMetrics).toInt()
            parentView.getWindowVisibleDisplayFrame(rect)
            val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
            val isShown = heightDiff >= estimatedKeyboardHeight

            if (isShown == alreadyOpen) {
                Log.d("TAG", "Ignore global layout change")
                return
            }

            alreadyOpen = isShown
            listener.onVisibilityChanged(isShown)
        }
    }
    parentView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun destroy() {
            parentView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
            lifecycle.removeObserver(this)
        }
    })
}





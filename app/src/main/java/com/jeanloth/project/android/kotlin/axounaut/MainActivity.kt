package com.jeanloth.project.android.kotlin.axounaut

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.onClick


class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private val mainVM : MainVM by viewModel()

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        mainVM.headerTitleLiveData().observe(this, {
            Log.d("[Main Activity]", "Title observed : $it")
            tv_header_title.text = it
        })

        bt_tb_menu_more.setOnClickListener {
            openPopUpMenu(it)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_article_details) {
                //fab_menu.visibility = GONE
            } else {
                //fab_menu.visibility = VISIBLE
            }
        }

        iv_header_logo.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.navigation_home)
            mainVM.setHeaderTitle("Kreyol Baker")
        }

    }

    fun navigateToCommands(view : View){
        navController.navigate(R.id.navigation_home)
    }

    fun navigateToStock(view : View){
        navController.navigate(R.id.navigation_dashboard)
    }

    fun navigateToAnalysis(view : View){
        navController.navigate(R.id.navigation_dashboard)
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun openPopUpMenu(view : View) {
        view.setOnClickListener {
            //Creating the instance of PopupMenu
            val popup = PopupMenu(this, view)

            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.toolbar_menu, popup.menu)

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_clients -> {
                        navController.navigateUp() // to clear previous navigation history
                        navController.navigate(R.id.nav_clients)
                    }
                    else -> {
                    }

                }
                true
            }
            popup.show() //showing popup menu
        }
    }

    fun hideOrShowMenuButton(makeVisible : Boolean){
        fab_menu.visibility = if(makeVisible) VISIBLE else GONE
    }


}
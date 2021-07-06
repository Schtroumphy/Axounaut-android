package com.jeanloth.project.android.kotlin.axounaut

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private val mainVM : MainVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        fab_add_command.setOnClickListener {
            AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }

        mainVM.headerTitleLiveData().observe(this, {
            Log.d("[Main Activity]", "Title observed : $it")
            tv_header_title.text = it
        })

        bt_tb_menu_more.setOnClickListener {
            openPopUpMenu(it)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_article_details) {
                bottomAppBar.visibility = GONE
                fab_add_command.visibility = GONE
                bottom_nav_view.visibility = GONE
            } else {
                bottomAppBar.visibility = VISIBLE
                fab_add_command.visibility = VISIBLE
                bottom_nav_view.visibility = VISIBLE
            }
        }

        iv_header_logo.setOnClickListener {
            navController.navigateUp()
            navController.navigate(R.id.navigation_home)
            mainVM.setHeaderTitle("Kreyol Baker")
        }


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
                        Toast.makeText(this, "Ouvrir l'onglet clients", Toast.LENGTH_SHORT).show()
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
}
package com.jeanloth.project.android.kotlin.axounaut

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.PayCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.imageDrawable
import splitties.views.onClick


class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController
    private val mainVM : MainVM by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host_fragment)

        when(intent.getStringExtra("FRAGMENT_TO_SHOW")){
            "COMMANDS" -> navigateToCommands()
            "ANALYSIS" -> navigateToAnalysis()
            "STOCK" -> navigateToStock()
            else -> "Kreyol Baker"
        }

        mainVM.headerTitleLiveData().observe(this, {
            Log.d("[Main Activity]", "Title observed : $it")
            tv_header_title.text = it.first
            tv_header_subtitle.text = it.second
        })

        bt_tb_menu_more.setOnClickListener {
            openPopUpMenu(it)
        }

        navController.addOnDestinationChangedListener { _, destination, classe ->
            if(destination.id == R.id.nav_article_details) {
                //fab_menu.visibility = GONE
            } else {
                //fab_menu.visibility = VISIBLE
            }
            hideOrShowAddCommandButton(destination.id == R.id.nav_command_list)
        }

        iv_header_logo.onClick {
            onBackPressed()
        }

        fab_add_command.onClick {
            displayAddCommandFragment()
        }
    }

    fun navigateToCommands(){
        navController.navigate(R.id.nav_command_list)
    }

    fun navigateToStock(){
        navController.navigate(R.id.nav_stock)
    }

    fun navigateToAnalysis(){
        navController.navigate(R.id.nav_analysis)
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

    fun hideOrShowAddCommandButton(makeVisible : Boolean){
        fab_add_command.visibility = if(makeVisible) VISIBLE else GONE
    }

    fun displayAddCommandFragment(){
        AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
    }

    fun displayPayCommandFragment(commandId : Long){
        PayCommandDialogFragment.newInstance(commandId).show(supportFragmentManager, "payDialog")
    }

    fun replaceHeaderLogoByBackButton(replaceByBackButton : Boolean){
        iv_header_logo.imageDrawable = ContextCompat.getDrawable(this, if(replaceByBackButton) R.drawable.ic_back_button else R.drawable.logo_kb_002)
        iv_header_logo.layoutParams.width = if(replaceByBackButton) 46 else 130
        iv_header_logo.layoutParams.height = if(replaceByBackButton) 46 else 130
    }


}
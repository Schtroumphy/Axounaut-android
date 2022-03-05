package com.jeanloth.project.android.kotlin.axounaut

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.jeanloth.project.android.kotlin.axounaut.AppLogger.logD
import com.jeanloth.project.android.kotlin.axounaut.Constants.ANALYSIS
import com.jeanloth.project.android.kotlin.axounaut.Constants.COMMANDS
import com.jeanloth.project.android.kotlin.axounaut.Constants.FRAGMENT_TO_SHOW
import com.jeanloth.project.android.kotlin.axounaut.Constants.STOCK
import com.jeanloth.project.android.kotlin.axounaut.databinding.ActivityMainBinding
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.PayCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import org.koin.android.viewmodel.ext.android.viewModel
import splitties.views.imageDrawable
import splitties.views.onClick

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val mainVM: MainVM by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        when (intent.getStringExtra(FRAGMENT_TO_SHOW)) {
            COMMANDS -> navigateToCommands()
            ANALYSIS -> navigateToAnalysis()
            STOCK -> navigateToStock()
            else -> throw IllegalArgumentException("Fragment to show not recognized.")
        }

        mainVM.headerTitleLiveData().observe(this, {
            javaClass.logD("Title observed : ${it.first}")

            binding.tvHeaderTitle.text = it.first
            binding.tvHeaderSubtitle.text = it.second
        })

        binding.btTbMenuMore.setOnClickListener {
            openPopUpMenu(it)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            hideOrShowAddCommandButton(destination.id == R.id.nav_command_list)
            binding.llMainHeader.visibility = if (destination.id == R.id.nav_home) GONE else VISIBLE
        }

        binding.ivHeaderLogo.onClick {
            onBackPressed()
        }

        binding.fabAddCommand.onClick {
            displayAddCommandFragment()
        }
    }

    private fun navigateToCommands(){
        navController.navigate(R.id.nav_command_list)
    }

    private fun navigateToStock(){
        navController.navigate(R.id.nav_stock)
    }

    private fun navigateToAnalysis(){
        navController.navigate(R.id.nav_analysis)
    }

    private fun openPopUpMenu(view : View) {
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
                }
                true
            }
            popup.show() //showing popup menu
        }
    }

    private fun hideOrShowAddCommandButton(makeVisible : Boolean){
        binding.fabAddCommand.visibility = if(makeVisible) VISIBLE else GONE
    }

    private fun displayAddCommandFragment(){
        AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
    }

    fun displayPayCommandFragment(commandId : Long){
        PayCommandDialogFragment.newInstance(commandId).show(supportFragmentManager, "payDialog")
    }

    fun replaceHeaderLogoByBackButton(replaceByBackButton : Boolean){
        binding.ivHeaderLogo.imageDrawable = ContextCompat.getDrawable(this, if(replaceByBackButton) R.drawable.ic_back_button else R.drawable.logo_kb_002)
        binding.ivHeaderLogo.layoutParams.width = if(replaceByBackButton) 46 else 130
        binding.ivHeaderLogo.layoutParams.height = if(replaceByBackButton) 46 else 130
    }
}
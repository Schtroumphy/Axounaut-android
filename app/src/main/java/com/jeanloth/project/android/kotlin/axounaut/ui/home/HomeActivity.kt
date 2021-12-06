package com.jeanloth.project.android.kotlin.axounaut.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.databinding.ActivityHomeBinding
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.CommandListFragmentDirections
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.fab_add_command
import kotlinx.android.synthetic.main.activity_main.*
import splitties.views.onClick

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private var fragmentToShow : String = "COMMANDS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvCommands.onClick {
            fragmentToShow = "COMMANDS"
            goToMainActivity()
        }

        binding.cvAnalysis.onClick {
            fragmentToShow = "ANALYSIS"
            goToMainActivity()
        }

        binding.cvStock.onClick {
            fragmentToShow = "STOCK"
            goToMainActivity()
        }

        fab_add_command.onClick {
            AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }

    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra("FRAGMENT_TO_SHOW", fragmentToShow)
        })
    }
}
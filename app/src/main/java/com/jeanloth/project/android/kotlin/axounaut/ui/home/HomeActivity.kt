package com.jeanloth.project.android.kotlin.axounaut.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeanloth.project.android.kotlin.axounaut.Constants.COMMANDS
import com.jeanloth.project.android.kotlin.axounaut.Constants.FRAGMENT_TO_SHOW
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.databinding.ActivityHomeBinding

class  HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private var fragmentToShow : String = COMMANDS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.cvCommands.onClick {
            fragmentToShow = COMMANDS
            goToMainActivity()
        }

        binding.cvAnalysis.onClick {
            fragmentToShow = ANALYSIS
            goToMainActivity()
        }

        binding.cvStock.onClick {
            fragmentToShow = STOCK
            goToMainActivity()
        }

        fab_add_command.onClick {
            AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }*/

    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            putExtra(FRAGMENT_TO_SHOW, fragmentToShow)
        })
    }
}
package com.jeanloth.project.android.kotlin.axounaut

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.AddCommandDialogFragment
import kotlinx.android.synthetic.main.activity_main.fab_add_command

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        fab_add_command.setOnClickListener {
            AddCommandDialogFragment.newInstance().show(supportFragmentManager, "dialog")
        }
    }
}
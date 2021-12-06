package com.jeanloth.project.android.kotlin.axounaut.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.CommandListFragment

class CommandViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragmentNames = listOf("En cours", "A venir", "Historique")

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> CommandListFragment()
            1 -> CommandListFragment()
            2 -> CommandListFragment()
            else -> CommandListFragment()
        }
    }

    override fun getCount(): Int {
        return fragmentNames.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentNames[position]
    }
}
package com.jeanloth.project.android.kotlin.axounaut.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jeanloth.project.android.kotlin.axounaut.ui.CommandListFragment

class CommandViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragmentNames = listOf("En cours", "A venir", "Historique")

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> CommandListFragment( displayMode = CommandListFragment.CommandDisplayMode.IN_PROGRESS)
            1 -> CommandListFragment(displayMode = CommandListFragment.CommandDisplayMode.TO_COME)
            2 -> CommandListFragment(displayMode = CommandListFragment.CommandDisplayMode.PAST)
            else -> CommandListFragment( displayMode = CommandListFragment.CommandDisplayMode.IN_PROGRESS)
        }
    }

    override fun getCount(): Int {
        return fragmentNames.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentNames[position]
    }
}
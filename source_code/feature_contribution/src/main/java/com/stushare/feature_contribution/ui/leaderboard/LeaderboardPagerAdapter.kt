package com.stushare.feature_contribution.ui.leaderboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LeaderboardPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RankedUsersFragment()
            1 -> RankedDocsFragment()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
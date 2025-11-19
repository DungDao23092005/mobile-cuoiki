package com.stushare.feature_contribution.ui.leaderboard

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.MainActivity

class LeaderboardFragment : Fragment(R.layout.fragment_leaderboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_leaderboard)
        val viewPager = view.findViewById<ViewPager2>(R.id.pager_leaderboard)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back_leaderboard)

        viewPager.adapter = LeaderboardPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Top người đóng góp"
                1 -> "Top tài liệu"
                else -> null
            }
        }.attach()

        btnBack.setOnClickListener {
            (activity as? MainActivity)?.openFragment(
                com.stushare.feature_contribution.ui.account.ProfileFragment(),
                addToBackStack = false
            )
        }
    }
}
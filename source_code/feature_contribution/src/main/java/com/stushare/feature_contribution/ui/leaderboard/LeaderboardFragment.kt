package com.stushare.feature_contribution.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.stushare.feature_contribution.ui.theme.StuShareTheme

class LeaderboardFragment : Fragment() {

    private val viewModel: LeaderboardViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                StuShareTheme {
                    LeaderboardScreen(
                        viewModel = viewModel,
                        onBackClick = { parentFragmentManager.popBackStack() }
                    )
                }
            }
        }
    }
}
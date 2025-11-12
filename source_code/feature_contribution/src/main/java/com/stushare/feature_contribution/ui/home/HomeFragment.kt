package com.stushare.feature_contribution.ui.home

import android.os.Bundle
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R
import android.widget.TextView

class HomeFragment : Fragment(R.layout.fragment_simple_text) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_simple).text = "Đây là file Home"
    }
}

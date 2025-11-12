package com.stushare.feature_contribution.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R
import android.widget.TextView

class SearchFragment : Fragment(R.layout.fragment_simple_text) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_simple).text = "Đây là file Search"
    }
}

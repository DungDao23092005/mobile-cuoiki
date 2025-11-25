package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class SwitchAccountFragment : Fragment(R.layout.fragment_switch_account) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Nút Thêm tài khoản
        view.findViewById<LinearLayout>(R.id.btn_add_account).setOnClickListener {
            // Ở đây bạn sẽ mở màn hình Đăng nhập (LoginActivity)
            // Ví dụ demo:
            Toast.makeText(context, "Chuyển đến màn hình Đăng nhập...", Toast.LENGTH_SHORT).show()
        }
    }
}
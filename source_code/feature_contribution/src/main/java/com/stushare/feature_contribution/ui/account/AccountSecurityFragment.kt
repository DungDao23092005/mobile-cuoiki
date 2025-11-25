package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class AccountSecurityFragment : Fragment(R.layout.fragment_account_security) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nút Back: Quay lại màn hình Settings
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 1. Mục "Thông tin cá nhân" -> Mở màn hình Form (PersonalInfoFragment)
        view.findViewById<View>(R.id.item_personal_info).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, PersonalInfoFragment()) // Đây là màn hình bạn làm lúc đầu
                .addToBackStack(null)
                .commit()
        }

        // Các mục khác (Demo)
        view.findViewById<View>(R.id.item_change_pass).setOnClickListener {
            Toast.makeText(context, "Tính năng Đổi mật khẩu", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.item_delete_account).setOnClickListener {
            Toast.makeText(context, "Xóa tài khoản", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Nút Tìm kiếm (nếu cần)
        view.findViewById<View>(R.id.btn_search_settings)?.setOnClickListener {
            Toast.makeText(context, "Tìm kiếm cài đặt...", Toast.LENGTH_SHORT).show()
        }

        // 1. Tài khoản và bảo mật -> Vào màn PersonalInfoFragment
        view.findViewById<View>(R.id.item_account).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, AccountSecurityFragment())
                .addToBackStack(null)
                .commit()
        }

        // 2. Thông báo
        view.findViewById<View>(R.id.item_noti).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, NotiSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        // 3. Giao diện
        view.findViewById<View>(R.id.item_appearance).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, AppearanceSettingsFragment())
                .addToBackStack(null)
                .commit()
        }

        // 4. Thông tin về StuShare
        view.findViewById<View>(R.id.item_about).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, AboutAppFragment()) // Mở màn hình vừa tạo
                .addToBackStack(null)
                .commit()
        }

        // 5. Liên hệ hỗ trợ
        view.findViewById<View>(R.id.item_support).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, ContactSupportFragment()) // Mở ContactSupportFragment
                .addToBackStack(null)
                .commit()
        }

        // 6. Báo cáo vi phạm
        view.findViewById<View>(R.id.item_report).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, ReportViolationFragment())
                .addToBackStack(null)
                .commit()
        }

        // 7. Chuyển tài khoản
        view.findViewById<View>(R.id.item_switch_account).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, SwitchAccountFragment())
                .addToBackStack(null)
                .commit()
        }

        // Nút Đăng xuất
        view.findViewById<View>(R.id.btn_logout).setOnClickListener {
            Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
            // Logic đăng xuất thực tế
        }
    }
}
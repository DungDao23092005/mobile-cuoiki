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
                .replace(R.id.main_nav_host, PersonalInfoFragment())
                .addToBackStack(null)
                .commit()
        }

        // 2. Thông báo
        view.findViewById<View>(R.id.item_noti).setOnClickListener {
            Toast.makeText(context, "Cài đặt Thông báo", Toast.LENGTH_SHORT).show()
        }

        // 3. Giao diện
        view.findViewById<View>(R.id.item_appearance).setOnClickListener {
            Toast.makeText(context, "Cài đặt Giao diện", Toast.LENGTH_SHORT).show()
        }

        // 4. Thông tin về StuShare
        view.findViewById<View>(R.id.item_about).setOnClickListener {
            Toast.makeText(context, "Phiên bản 1.0.0", Toast.LENGTH_SHORT).show()
        }

        // 5. Liên hệ hỗ trợ
        view.findViewById<View>(R.id.item_support).setOnClickListener {
            Toast.makeText(context, "Mở chat hỗ trợ", Toast.LENGTH_SHORT).show()
        }

        // 6. Báo cáo vi phạm
        view.findViewById<View>(R.id.item_report).setOnClickListener {
            Toast.makeText(context, "Báo cáo vi phạm", Toast.LENGTH_SHORT).show()
        }

        // 7. Chuyển tài khoản
        view.findViewById<View>(R.id.item_switch_account).setOnClickListener {
            Toast.makeText(context, "Chuyển tài khoản", Toast.LENGTH_SHORT).show()
        }

        // Nút Đăng xuất
        view.findViewById<View>(R.id.btn_logout).setOnClickListener {
            Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
            // Logic đăng xuất thực tế
        }
    }
}
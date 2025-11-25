package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.stushare.feature_contribution.R

class NotiSettingsFragment : Fragment(R.layout.fragment_noti_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Xử lý nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Ví dụ xử lý sự kiện khi bật/tắt switch (nếu cần)
        val swPush = view.findViewById<SwitchMaterial>(R.id.sw_push_noti)
        swPush.setOnCheckedChangeListener { _, isChecked ->
            val msg = if (isChecked) "Đã bật thông báo đẩy" else "Đã tắt thông báo đẩy"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

        // Bạn có thể thêm logic lưu trạng thái (SharedPreferences) ở đây cho các switch khác
    }
}
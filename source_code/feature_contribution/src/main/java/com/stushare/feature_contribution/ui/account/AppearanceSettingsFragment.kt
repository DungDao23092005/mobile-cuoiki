package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Import AlertDialog
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class AppearanceSettingsFragment : Fragment(R.layout.fragment_appearance_settings) {

    private lateinit var tvCurrentFontSize: TextView
    private var selectedFontSizeIndex = 1 // Mặc định là 1 (Vừa)
    private val fontSizes = arrayOf("Nhỏ", "Vừa", "Lớn")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ... (Code nút Back và RadioGroup Theme/Language giữ nguyên) ...
        view.findViewById<View>(R.id.btn_back).setOnClickListener { parentFragmentManager.popBackStack() }

        tvCurrentFontSize = view.findViewById(R.id.tv_current_font_size)

        // Xử lý click vào mục Đổi cỡ chữ
        view.findViewById<LinearLayout>(R.id.item_font_size).setOnClickListener {
            showFontSizeDialog()
        }
    }

    private fun showFontSizeDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chọn cỡ chữ")
        builder.setSingleChoiceItems(fontSizes, selectedFontSizeIndex) { dialog, which ->
            selectedFontSizeIndex = which
            // Cập nhật text hiển thị bên ngoài
            tvCurrentFontSize.text = fontSizes[which]

            // Thông báo (Sau này bạn sẽ gọi hàm set theme thực tế ở đây)
            Toast.makeText(context, "Đã chọn cỡ chữ: ${fontSizes[which]}", Toast.LENGTH_SHORT).show()

            dialog.dismiss()
        }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }
}
package com.stushare.feature_contribution.ui.account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Chính sách bảo mật -> Mở Web
        view.findViewById<View>(R.id.item_privacy_policy).setOnClickListener {
            openUrl("https://www.google.com") // Thay bằng link chính sách của bạn
        }

        // 3. Điều khoản dịch vụ -> Mở Web
        view.findViewById<View>(R.id.item_terms).setOnClickListener {
            openUrl("https://www.google.com") // Thay bằng link điều khoản của bạn
        }

        // 4. Đánh giá ứng dụng -> Toast demo
        view.findViewById<View>(R.id.item_rate_app).setOnClickListener {
            Toast.makeText(context, "Cảm ơn bạn đã đánh giá 5 sao!", Toast.LENGTH_SHORT).show()
            // Thực tế có thể mở link CH Play: market://details?id=your.package.name
        }

        // 5. Website -> Mở Web
        view.findViewById<View>(R.id.item_website).setOnClickListener {
            openUrl("https://stushare.vn") // Thay bằng website thật
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Không thể mở trình duyệt", Toast.LENGTH_SHORT).show()
        }
    }
}
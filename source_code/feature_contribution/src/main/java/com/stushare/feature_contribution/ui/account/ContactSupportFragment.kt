package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class ContactSupportFragment : Fragment(R.layout.fragment_contact_support) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Xử lý click vào 4 ô chức năng
        view.findViewById<View>(R.id.card_faq).setOnClickListener {
            Toast.makeText(context, "Xem tất cả câu hỏi thường gặp", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.card_send_request).setOnClickListener {
            Toast.makeText(context, "Gửi yêu cầu hỗ trợ", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.card_chat_online).setOnClickListener {
            Toast.makeText(context, "Bắt đầu Chat trực tuyến", Toast.LENGTH_SHORT).show()
        }
        view.findViewById<View>(R.id.card_hotline).setOnClickListener {
            Toast.makeText(context, "Gọi tổng đài 1900 xxxx", Toast.LENGTH_SHORT).show()
        }

        // 3. Xử lý đóng mở câu hỏi (FAQ Expandable)
        setupExpandableItem(
            view.findViewById(R.id.layout_q1_header),
            view.findViewById(R.id.tv_q1_content),
            view.findViewById(R.id.img_q1_arrow)
        )
        setupExpandableItem(
            view.findViewById(R.id.layout_q2_header),
            view.findViewById(R.id.tv_q2_content),
            view.findViewById(R.id.img_q2_arrow)
        )
        setupExpandableItem(
            view.findViewById(R.id.layout_q3_header),
            view.findViewById(R.id.tv_q3_content),
            view.findViewById(R.id.img_q3_arrow)
        )

        // 4. Nút FAB Chat
        view.findViewById<View>(R.id.fab_chat).setOnClickListener {
            Toast.makeText(context, "Mở cửa sổ chat nhanh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupExpandableItem(header: LinearLayout, content: TextView, arrow: ImageView) {
        header.setOnClickListener {
            if (content.visibility == View.VISIBLE) {
                // Đang mở -> Đóng lại
                content.visibility = View.GONE
                arrow.setImageResource(R.drawable.ic_arrow_right) // Icon mũi tên ngang
            } else {
                // Đang đóng -> Mở ra
                content.visibility = View.VISIBLE
                arrow.setImageResource(R.drawable.ic_arrow_down) // Icon mũi tên xuống (bạn cần tạo icon này)
            }
        }
    }
}
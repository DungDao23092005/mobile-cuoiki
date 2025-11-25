package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.R

class ReportViolationFragment : Fragment(R.layout.fragment_report_violation) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Nút Back
        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Setup Spinner (Danh sách loại vi phạm)
        val spinner = view.findViewById<Spinner>(R.id.spinner_violation_type)
        val violationTypes = listOf(
            "Spam hoặc quảng cáo",
            "Nội dung không phù hợp",
            "Quấy rối hoặc bắt nạt",
            "Thông tin sai lệch",
            "Vi phạm bản quyền",
            "Khác"
        )

        // Tạo Adapter đơn giản cho Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, violationTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // 3. Xử lý nút Thêm ảnh (Demo)
        view.findViewById<View>(R.id.layout_add_image).setOnClickListener {
            Toast.makeText(context, "Mở thư viện ảnh...", Toast.LENGTH_SHORT).show()
        }

        // 4. Nút Gửi báo cáo
        view.findViewById<View>(R.id.btn_submit_report).setOnClickListener {
            Toast.makeText(context, "Đã gửi báo cáo thành công!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack() // Quay lại màn hình trước
        }
    }
}
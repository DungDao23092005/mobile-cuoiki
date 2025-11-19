package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels // Nếu muốn kết nối ViewModel
import com.stushare.feature_contribution.R

class PersonalInfoFragment : Fragment(R.layout.fragment_personal_info) {

    // Có thể dùng chung ProfileViewModel để load/save data
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btn_back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val etName = view.findViewById<EditText>(R.id.et_name)
        val etDob = view.findViewById<EditText>(R.id.et_dob)
        val btnSave = view.findViewById<Button>(R.id.btn_save)

        // Logic lưu
        btnSave.setOnClickListener {
            val newName = etName.text.toString()
            Toast.makeText(context, "Đã lưu thông tin: $newName", Toast.LENGTH_SHORT).show()
            // TODO: Gọi viewModel.updateProfile(...)
            parentFragmentManager.popBackStack()
        }
    }
}
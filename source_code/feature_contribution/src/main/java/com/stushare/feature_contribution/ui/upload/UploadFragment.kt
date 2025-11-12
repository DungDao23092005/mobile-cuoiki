// trong com.stushare.feature_contribution.ui.upload
package com.stushare.feature_contribution.ui.upload

import com.stushare.feature_contribution.ui.noti.NotificationItem
import com.stushare.feature_contribution.ui.noti.NotiFragment

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.stushare.feature_contribution.MainActivity
import com.stushare.feature_contribution.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UploadFragment : Fragment(R.layout.fragment_upload), MainActivity.FabClickListener {

    private var selectedUri: Uri? = null

    // 1. Khởi tạo ViewModel (Sẽ hoạt động sau khi bạn thêm dependency ở Bước 1)
    private val viewModel: UploadViewModel by viewModels()

    private val pickFileLauncher = registerForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? -> uri?.let { onFilePicked(it) } }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnChoose = view.findViewById<Button>(R.id.btn_choose)
        val btnUpload = view.findViewById<Button>(R.id.btn_upload)
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val etDesc = view.findViewById<EditText>(R.id.et_desc)
        val titleEt = view.findViewById<EditText>(R.id.et_title)

        // initial state
        btnUpload.isEnabled = false
        btnUpload.alpha = 0.6f

        // --- Thiết lập cho EditText mô tả: cố định chiều cao, bật scrollbar và cho phép cuộn nội bộ ---
        etDesc.isVerticalScrollBarEnabled = true
        etDesc.setOnTouchListener { v, event ->
            v.parent?.requestDisallowInterceptTouchEvent(true)
            if (event.action == MotionEvent.ACTION_UP) {
                v.parent?.requestDisallowInterceptTouchEvent(false)
            }
            false
        }
        etDesc.isFocusableInTouchMode = true
        // --- (Hết code TouchListener) ---

        btnChoose.setOnClickListener {
            pickFileLauncher.launch(arrayOf(
                "application/pdf",
                "application/msword",
                "application/vnd.ms-powerpoint",
                "application/vnd.ms-excel",
                "application.vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            ))
        }

        // 2. Logic Upload được chuyển cho ViewModel
        btnUpload.setOnClickListener {
            val title = titleEt.text?.toString()?.trim() ?: ""
            val desc = etDesc.text?.toString()?.trim()

            if (selectedUri == null) {
                Toast.makeText(requireContext(), "Bạn chưa chọn file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tiêu đề", Toast.LENGTH_SHORT).show()
                titleEt.requestFocus()
                return@setOnClickListener
            }

            // Chỉ cần gọi ViewModel, không cần lifecycleScope ở đây
            viewModel.handleUploadClick(title, desc)
        }

        // 3. Lắng nghe StateFlow và SharedFlow từ ViewModel
        observeViewModel(view)

        // --- (Code cho btnCancel và btnBack giữ nguyên) ---
        btnCancel.setOnClickListener {
            selectedUri = null
            resetUiToEmptyState(view)
            Toast.makeText(requireContext(), "Đã hủy", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            // Luôn quay lại màn hình HomeFragment (gọn bằng gọi MainActivity)
            val mainActivity = activity as? com.stushare.feature_contribution.MainActivity
            mainActivity?.let {
                it.supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
                it.supportFragmentManager.beginTransaction()
                    .replace(R.id.main_nav_host, com.stushare.feature_contribution.ui.home.HomeFragment())
                    .commit()
            }
        }
    }

    /**
     * Hàm mới để lắng nghe tất cả các Flow từ ViewModel
     */
    private fun observeViewModel(rootView: View) {
        // Sử dụng viewLifecycleOwner cho coroutines
        viewLifecycleOwner.lifecycleScope.launch {
            // repeatOnLifecycle để đảm bảo coroutine chỉ chạy khi Fragment ở trạng thái STARTED
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Lắng nghe trạng thái loading
                launch {
                    viewModel.isUploading.collect { isLoading ->
                        showProgress(isLoading)
                    }
                }

                // Lắng nghe sự kiện (thành công / lỗi)
                launch {
                    viewModel.uploadEvent.collect { result ->
                        when (result) {
                            is UploadViewModel.UploadResult.Success -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                                resetUiToEmptyState(rootView) // Reset UI
                                selectedUri = null // Xóa file đã chọn
                            }
                            is UploadViewModel.UploadResult.Error -> {
                                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // --- (Tất cả các hàm private helper khác giữ nguyên) ---

    override fun onFabClicked() {
        // xử lý khi người dùng bấm FAB (ở Activity), khi fragment đang hiện
        // Thực hiện hành động tương tự như bấm nút upload trong fragment
        val viewRoot = view ?: return
        val btnUpload = viewRoot.findViewById<Button>(R.id.btn_upload)
        if (btnUpload.isEnabled) {
            btnUpload.performClick()
        } else {
            Toast.makeText(requireContext(), "Bạn chưa chọn file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onFilePicked(uri: Uri) {
        selectedUri = uri
        val root = view ?: return
        val activity = requireActivity()

        val fileName = queryName(activity, uri) ?: "Đã chọn file"

        root.findViewById<TextView>(R.id.tv_selected_name).text = fileName

        // gợi ý tiêu đề (filename không có extension)

        // <<<--- DÒNG BỊ THIẾU CỦA BẠN ĐÃ ĐƯỢC THÊM LẠI Ở ĐÂY ---
        val suggestedTitle = stripExtension(fileName)
        // --- DÒNG TRÊN --- >>>

        root.findViewById<EditText>(R.id.et_title).setText(suggestedTitle)

        // enable Upload button
        val btnUpload = root.findViewById<Button>(R.id.btn_upload)
        btnUpload.isEnabled = true
        btnUpload.alpha = 1f
    }

    private fun resetUiToEmptyState(root: View) {
        root.findViewById<TextView>(R.id.tv_selected_name).text = "Chưa có file"
        root.findViewById<EditText>(R.id.et_title).setText("")
        root.findViewById<EditText>(R.id.et_desc).setText("")
        val btnUpload = root.findViewById<Button>(R.id.btn_upload)
        btnUpload.isEnabled = false
        btnUpload.alpha = 0.6f
    }

    private fun showProgress(show: Boolean) {
        view?.findViewById<View>(R.id.progress_overlay)?.isVisible = show
    }

    // Try to read _display_name; fallback to lastPathSegment
    private fun queryName(activity: Activity, uri: Uri): String? {
        var result: String? = null
        val projection = arrayOf("_display_name")
        var cursor: Cursor? = null
        try {
            cursor = activity.contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idx = it.getColumnIndex("_display_name")
                    if (idx >= 0) result = it.getString(idx)
                }
            }
        } catch (e: Exception) {
            // ignore
        } finally {
            cursor?.close()
        }
        if (result == null) result = uri.lastPathSegment
        return result
    }

    private fun stripExtension(name: String): String {
        val idx = name.lastIndexOf('.')
        return if (idx > 0) name.substring(0, idx) else name
    }
}
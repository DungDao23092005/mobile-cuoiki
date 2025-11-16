package com.stushare.feature_contribution.ui.noti

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stushare.feature_contribution.R

import androidx.fragment.app.viewModels // <-- THÊM
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class NotiFragment : Fragment(R.layout.fragment_noti) {

    // Khởi tạo ViewModel
    private val viewModel: NotiViewModel by viewModels()

    private lateinit var adapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_notif)
        adapter = NotificationAdapter()
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notifications.collect { items ->
                    adapter.setAll(items)
                }
            }
        }
        viewModel.markAllNotificationsAsRead()
    }

    /** Cho phép Activity/Fragment khác thêm thông báo mới */
    fun addNotification(item: NotificationItem) {
        // Hàm này không còn cần thiết nếu dùng ViewModel, nhưng vẫn giữ để tránh lỗi.
        // Dữ liệu mới sẽ được thêm vào DB qua MyFirebaseMessagingService
        // hoặc UploadViewModel, sau đó tự động hiển thị ở đây qua Flow.
    }
}
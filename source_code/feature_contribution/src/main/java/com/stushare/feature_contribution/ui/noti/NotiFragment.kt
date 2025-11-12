package com.stushare.feature_contribution.ui.noti

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stushare.feature_contribution.R

class NotiFragment : Fragment(R.layout.fragment_noti) {

    private lateinit var adapter: NotificationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rv = view.findViewById<RecyclerView>(R.id.rv_notif)
        adapter = NotificationAdapter()
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // ví dụ item mẫu
        adapter.addAtTop(
            NotificationItem(
                title = "Chào mừng",
                message = "Bạn đã mở Thông báo",
                type = NotificationItem.Type.INFO
            )
        )
    }

    /** Cho phép Activity/Fragment khác thêm thông báo mới */
    fun addNotification(item: NotificationItem) {
        adapter.addAtTop(item)
    }
}

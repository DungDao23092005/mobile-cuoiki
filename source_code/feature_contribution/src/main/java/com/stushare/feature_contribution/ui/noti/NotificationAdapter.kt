package com.stushare.feature_contribution.ui.noti

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stushare.feature_contribution.R

class NotificationAdapter(
    private val items: MutableList<NotificationItem> = mutableListOf()
) : RecyclerView.Adapter<NotificationAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val ivIcon: ImageView = v.findViewById(R.id.iv_notif_icon)
        val tvTime: TextView = v.findViewById(R.id.tv_notif_time)
        val tvTitle: TextView = v.findViewById(R.id.tv_notif_title)
        val tvMsg: TextView = v.findViewById(R.id.tv_notif_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, position: Int) {
        val it = items[position]
        h.tvTime.text = it.time
        h.tvTitle.text = it.title
        h.tvMsg.text  = it.message
    }

    override fun getItemCount() = items.size

    fun addAtTop(item: NotificationItem) {
        items.add(0, item)
        notifyItemInserted(0)
    }

    fun setAll(newItems: List<NotificationItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

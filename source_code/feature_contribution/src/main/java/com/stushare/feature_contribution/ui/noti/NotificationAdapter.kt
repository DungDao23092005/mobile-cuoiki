package com.stushare.feature_contribution.ui.noti

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stushare.feature_contribution.R
import androidx.core.content.ContextCompat

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
        val item = items[position]
        h.tvTime.text = item.time
        h.tvTitle.text = item.title
        h.tvMsg.text  = item.message

        val iconRes = when (item.type) {
            NotificationItem.Type.SUCCESS -> R.drawable.ic_check_circle
            NotificationItem.Type.WARNING -> R.drawable.ic_warning
            NotificationItem.Type.INFO -> R.drawable.ic_notifications
        }

        val iconColor = when (item.type) {
            NotificationItem.Type.SUCCESS -> ContextCompat.getColor(h.itemView.context, R.color.green_success)
            NotificationItem.Type.WARNING -> ContextCompat.getColor(h.itemView.context, R.color.orange_warning)
            NotificationItem.Type.INFO -> ContextCompat.getColor(h.itemView.context, R.color.blue_info)
        }

        h.ivIcon.setImageResource(iconRes)
        h.ivIcon.setColorFilter(iconColor)
    }

    override fun getItemCount() = items.size

    fun setAll(newItems: List<NotificationItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.stushare.feature_contribution.R

// đổi tên trường tránh xung đột: docTitle thay cho title
data class DocItem(val docTitle: String, val meta: String)

class ProfileFragment : Fragment() {

    private val docsPublished = mutableListOf<DocItem>()
    private val docsSaved = mutableListOf<DocItem>()
    private val docsDownloaded = mutableListOf<DocItem>()

    private lateinit var adapter: DocAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // sample data
        repeat(6) {
            docsPublished.add(DocItem("Title môn học", "số lượt tải · Môn học"))
            docsSaved.add(DocItem("Đã lưu: Tài liệu $it", "số lượt tải · Môn học"))
            docsDownloaded.add(DocItem("Đã tải về: Tài liệu $it", "số lượt tải · Môn học"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup tabs
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_profile)
        tabLayout.addTab(tabLayout.newTab().setText("Tài liệu đã đăng"))
        tabLayout.addTab(tabLayout.newTab().setText("Đã lưu"))
        tabLayout.addTab(tabLayout.newTab().setText("Đã tải về"))

        // Recycler view
        val rv = view.findViewById<RecyclerView>(R.id.rv_docs)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = DocAdapter(mutableListOf())
        rv.adapter = adapter

        // default show published
        showDocsForTab(0)

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showDocsForTab(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // settings button (safe null-check)
        view.findViewById<ImageButton>(R.id.btn_settings)?.setOnClickListener {
            Toast.makeText(requireContext(), "Mở cài đặt (thêm chức năng)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDocsForTab(pos: Int) {
        val list = when(pos) {
            0 -> docsPublished
            1 -> docsSaved
            2 -> docsDownloaded
            else -> docsPublished
        }
        adapter.setAll(list)
    }

    // Adapter (dùng docTitle)
    class DocAdapter(private val items: MutableList<DocItem>): RecyclerView.Adapter<DocAdapter.VH>() {

        inner class VH(v: View): RecyclerView.ViewHolder(v) {
            val title: TextView = v.findViewById(R.id.tv_doc_title)
            val meta: TextView = v.findViewById(R.id.tv_doc_meta)
            val more: ImageButton = v.findViewById(R.id.btn_more)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_doc, parent, false)
            return VH(v)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.title.text = item.docTitle
            holder.meta.text = item.meta
            holder.more.setOnClickListener {
                // dùng concatenation để tránh bất kỳ vấn đề lookup tên biến
                Toast.makeText(holder.itemView.context, "More for " + item.docTitle, Toast.LENGTH_SHORT).show()
            }
        }


        override fun getItemCount(): Int = items.size

        fun setAll(newItems: List<DocItem>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }
    }
}

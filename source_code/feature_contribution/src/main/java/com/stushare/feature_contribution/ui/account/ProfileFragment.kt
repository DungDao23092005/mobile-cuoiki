package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu // <-- THÊM IMPORT
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.db.SavedDocumentEntity
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var adapter: DocAdapter
    private lateinit var tabLayout: TabLayout

    private var publishedDocsList: List<DocItem> = emptyList()
    private var savedDocsList: List<DocItem> = emptyList()
    private var downloadedDocsList: List<DocItem> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayout = view.findViewById(R.id.tab_profile)
        tabLayout.addTab(tabLayout.newTab().setText("Tài liệu đã đăng"))
        tabLayout.addTab(tabLayout.newTab().setText("Đã lưu"))
        tabLayout.addTab(tabLayout.newTab().setText("Đã tải về"))

        // Recycler view
        val rv = view.findViewById<RecyclerView>(R.id.rv_docs)
        rv.layoutManager = LinearLayoutManager(requireContext())

        // *** SỬA ĐỔI Ở ĐÂY ***
        // Truyền một lambda vào Adapter để xử lý sự kiện xóa
        adapter = DocAdapter(mutableListOf()) { item ->
            // Đây là code chạy khi người dùng bấm "Xóa"
            // (Chỉ xóa nếu item thuộc tab "Đã đăng")
            if (tabLayout.selectedTabPosition == 0) {
                viewModel.deletePublishedDocument(item.documentId)
            } else {
                Toast.makeText(requireContext(), "Chức năng Xóa cho tab này chưa được hỗ trợ", Toast.LENGTH_SHORT).show()
            }
        }
        rv.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showDocsForTab(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        view.findViewById<ImageButton>(R.id.btn_settings)?.setOnClickListener {
            Toast.makeText(requireContext(), "Mở cài đặt", Toast.LENGTH_SHORT).show()
        }

        observeViewModel(view)
    }

    private fun observeViewModel(view: View) {
        val tvProfileName = view.findViewById<TextView>(R.id.tv_profile_name)
        val tvProfileSub = view.findViewById<TextView>(R.id.tv_profile_sub)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.userProfile.collect { user ->
                        if (user != null) {
                            tvProfileName.text = "Xin chào, ${user.fullName}"
                        } else {
                            tvProfileName.text = "Xin chào, Khách"
                        }
                    }
                }

                launch {
                    viewModel.publishedDocuments.collect { docs ->
                        publishedDocsList = docs
                        if (tabLayout.selectedTabPosition == 0) {
                            showDocsForTab(0)
                        }
                    }
                }

                // Lắng nghe danh sách "Đã lưu" (Từ Room DB)
                launch {
                    viewModel.savedDocuments.collect { entities ->
                        // *** SỬA ĐỔI Ở ĐÂY ***
                        // Cập nhật mapping để dùng DocItem mới
                        savedDocsList = entities.map {
                            DocItem(it.documentId, it.title, it.metaInfo)
                        }
                        if (tabLayout.selectedTabPosition == 1) {
                            showDocsForTab(1)
                        }
                    }
                }

                launch {
                    viewModel.downloadedDocuments.collect { docs ->
                        downloadedDocsList = docs
                        if (tabLayout.selectedTabPosition == 2) {
                            showDocsForTab(2)
                        }
                    }
                }
            }
        }
    }

    private fun showDocsForTab(pos: Int) {
        val list = when (pos) {
            0 -> publishedDocsList
            1 -> savedDocsList
            2 -> downloadedDocsList
            else -> publishedDocsList
        }
        adapter.setAll(list)
    }

    // *** SỬA ĐỔI Ở ĐÂY ***
    // 1. Thêm (private val onDeleteClicked: (DocItem) -> Unit) vào constructor
    class DocAdapter(
        private val items: MutableList<DocItem>,
        private val onDeleteClicked: (DocItem) -> Unit // Lambda để gọi khi bấm xóa
    ) : RecyclerView.Adapter<DocAdapter.VH>() {

        inner class VH(v: View) : RecyclerView.ViewHolder(v) {
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

            // *** SỬA ĐỔI Ở ĐÂY ***
            // 2. Xóa Toast, thay bằng PopupMenu
            holder.more.setOnClickListener { view ->
                // Tạo PopupMenu
                val popup = PopupMenu(holder.itemView.context, view)
                // Thêm item "Xóa" vào menu
                popup.menu.add("Xóa")
                // (Bạn có thể thêm "Chỉnh sửa" ở đây sau)
                // popup.menu.add("Chỉnh sửa")

                // Đặt listener khi một item trong menu được chọn
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.title) {
                        "Xóa" -> {
                            // Gọi lambda, truyền item hiện tại ra Fragment
                            onDeleteClicked(item)
                            true
                        }
                        // "Chỉnh sửa" -> { ... true }
                        else -> false
                    }
                }
                // Hiển thị menu
                popup.show()
            }
        }

        override fun getItemCount(): Int = items.size

        fun setAll(newItems: List<DocItem>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged() // Tự động cập nhật khi xóa
        }
    }
}
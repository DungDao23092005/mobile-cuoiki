package com.stushare.feature_contribution.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
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
import com.stushare.feature_contribution.MainActivity
import com.stushare.feature_contribution.ui.leaderboard.LeaderboardFragment
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var adapter: DocAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var rvDocs: RecyclerView
    private lateinit var tvEmptyMessage: TextView

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

        rvDocs = view.findViewById(R.id.rv_docs)
        tvEmptyMessage = view.findViewById(R.id.tv_empty_message)

        rvDocs.layoutManager = LinearLayoutManager(requireContext())
        adapter = DocAdapter(mutableListOf()) { item ->
            if (tabLayout.selectedTabPosition == 0) {
                viewModel.deletePublishedDocument(item.documentId)
            } else {
                Toast.makeText(requireContext(), "Chức năng Xóa cho tab này chưa được hỗ trợ", Toast.LENGTH_SHORT).show()
            }
        }
        rvDocs.adapter = adapter

        // --- SỰ KIỆN CLICK NÚT BẢNG XẾP HẠNG ---
        view.findViewById<View>(R.id.btn_view_leaderboard)?.setOnClickListener {
            (activity as? MainActivity)?.openFragment(LeaderboardFragment())
        }
        // -----------------------------------------

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showDocsForTab(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        view.findViewById<ImageButton>(R.id.btn_settings)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_nav_host, SettingsFragment())
                .addToBackStack(null)
                .commit()
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
                            // Có thể thêm hiển thị điểm số ở đây nếu muốn
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
                launch {
                    viewModel.savedDocuments.collect { entities ->
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

        if (list.isEmpty()) {
            rvDocs.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE

            tvEmptyMessage.text = when (pos) {
                0 -> "Hiện không có tài liệu đã đăng"
                1 -> "Hiện không có tài liệu đã lưu"
                2 -> "Hiện không có tài liệu đã tải về"
                else -> "Không có tài liệu"
            }
        } else {
            rvDocs.visibility = View.VISIBLE
            tvEmptyMessage.visibility = View.GONE
        }
        adapter.setAll(list)
    }

    class DocAdapter(
        private val items: MutableList<DocItem>,
        private val onDeleteClicked: (DocItem) -> Unit
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

            holder.more.setOnClickListener { view ->
                val popup = PopupMenu(holder.itemView.context, view)
                popup.menu.add("Xóa")
                popup.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.title == "Xóa") {
                        onDeleteClicked(item)
                        true
                    } else false
                }
                popup.show()
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
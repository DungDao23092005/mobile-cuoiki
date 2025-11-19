package com.stushare.feature_contribution.ui.leaderboard

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stushare.feature_contribution.R
import kotlinx.coroutines.launch

class RankedDocsFragment : Fragment(R.layout.fragment_simple_text) {

    private val viewModel: LeaderboardViewModel by viewModels()
    private lateinit var adapter: RankedDocumentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val root = view as ViewGroup
        root.removeAllViews()

        val rv = RecyclerView(requireContext())
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = RankedDocumentAdapter(emptyList())
        rv.adapter = adapter
        root.addView(rv)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankedDocs.collect { docs ->
                adapter.updateData(docs)
            }
        }

        viewModel.fetchRankedDocs()
    }
}

class RankedDocumentAdapter(private var docs: List<RankedDocument>) :
    RecyclerView.Adapter<RankedDocumentAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val rank: TextView = v.findViewById(R.id.tv_rank)
        val title: TextView = v.findViewById(R.id.tv_doc_title)
        val author: TextView = v.findViewById(R.id.tv_doc_author)
        val downloads: TextView = v.findViewById(R.id.tv_doc_downloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranked_document, parent, false)
        return VH(v)
    }

    override fun getItemCount() = docs.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val doc = docs[position]
        holder.rank.text = doc.rank.toString()
        holder.title.text = doc.title
        holder.author.text = doc.author
        holder.downloads.text = "${doc.downloads} lượt"

        // Top 3 màu đặc biệt
        val color = when(doc.rank) {
            1 -> android.graphics.Color.parseColor("#FFD700")
            2 -> android.graphics.Color.parseColor("#C0C0C0")
            3 -> android.graphics.Color.parseColor("#CD7F32")
            else -> android.graphics.Color.BLACK
        }
        holder.rank.setTextColor(color)
    }

    fun updateData(newDocs: List<RankedDocument>) {
        docs = newDocs
        notifyDataSetChanged()
    }
}
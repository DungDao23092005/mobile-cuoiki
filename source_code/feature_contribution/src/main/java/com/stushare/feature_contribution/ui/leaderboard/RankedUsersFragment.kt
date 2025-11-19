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

class RankedUsersFragment : Fragment(R.layout.fragment_simple_text) {

    private val viewModel: LeaderboardViewModel by viewModels()
    private lateinit var adapter: RankedUserAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val root = view as ViewGroup
        root.removeAllViews()

        val rv = RecyclerView(requireContext())
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = RankedUserAdapter(emptyList())
        rv.adapter = adapter
        root.addView(rv)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.rankedUsers.collect { users ->
                adapter.updateData(users)
            }
        }

        viewModel.fetchRankedUsers()
    }
}

class RankedUserAdapter(private var users: List<RankedUser>) :
    RecyclerView.Adapter<RankedUserAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val rank: TextView = v.findViewById(R.id.tv_rank)
        val name: TextView = v.findViewById(R.id.tv_rank_name)
        val score: TextView = v.findViewById(R.id.tv_rank_score)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranked_user, parent, false)
        return VH(v)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = users[position]
        holder.rank.text = user.rank.toString()
        holder.name.text = user.name
        holder.score.text = "${user.score} điểm"

        // Top 3 có màu đặc biệt
        val color = when(user.rank) {
            1 -> android.graphics.Color.parseColor("#FFD700") // Vàng
            2 -> android.graphics.Color.parseColor("#C0C0C0") // Bạc
            3 -> android.graphics.Color.parseColor("#CD7F32") // Đồng
            else -> android.graphics.Color.BLACK
        }
        holder.rank.setTextColor(color)
    }

    fun updateData(newUsers: List<RankedUser>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
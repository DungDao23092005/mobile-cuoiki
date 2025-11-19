package com.stushare.feature_contribution

import com.stushare.feature_contribution.ui.upload.UploadFragment
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.ui.account.ProfileFragment
import com.stushare.feature_contribution.ui.home.HomeFragment
import com.stushare.feature_contribution.ui.noti.NotiFragment
import com.stushare.feature_contribution.ui.search.SearchFragment
import com.stushare.feature_contribution.ui.leaderboard.LeaderboardFragment // <--- MỚI
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.stushare.feature_contribution.db.AppDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    interface FabClickListener {
        fun onFabClicked()
    }

    private lateinit var fabUpload: ImageButton
    private lateinit var icHome: ImageButton
    private lateinit var icSearch: ImageButton
    private lateinit var icNoti: ImageButton
    private lateinit var icAccount: ImageButton
    private lateinit var notifBadge: TextView

    private val notificationDao by lazy {
        AppDatabase.getInstance(applicationContext).notificationDao()
    }

    private var bottomBarHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        icHome = findViewById(R.id.ic_home)
        icSearch = findViewById(R.id.ic_search)
        icNoti = findViewById(R.id.ic_notifications)
        icAccount = findViewById(R.id.ic_profile)
        fabUpload = findViewById(R.id.fab_upload)
        notifBadge = findViewById(R.id.tv_notif_badge)

        findViewById<View?>(R.id.fab_container)?.bringToFront()

        val bottomBarBg = findViewById<View>(R.id.bottom_bar_bg)
        bottomBarBg.post {
            val marginTopPx = 20.dpToPx()
            bottomBarHeight = bottomBarBg.height + marginTopPx
            applyBottomPaddingToFragments()
        }

        if (savedInstanceState == null) {
            openFragment(HomeFragment(), addToBackStack = false)
            setActiveIcon(R.id.ic_home)
        } else {
            updateActiveFromCurrentFragment()
        }

        icHome.setOnClickListener {
            openFragment(HomeFragment())
            setActiveIcon(R.id.ic_home)
        }
        icSearch.setOnClickListener {
            openFragment(SearchFragment())
            setActiveIcon(R.id.ic_search)
        }
        icNoti.setOnClickListener {
            openFragment(NotiFragment())
            setActiveIcon(R.id.ic_notifications)
        }
        icAccount.setOnClickListener {
            openFragment(ProfileFragment())
            setActiveIcon(R.id.ic_profile)
        }

        fabUpload.setOnClickListener {
            val current = supportFragmentManager.findFragmentById(R.id.main_nav_host)
            if (current is FabClickListener) {
                (current as FabClickListener).onFabClicked()
            } else {
                openFragment(UploadFragment())
                setActiveIcon(R.id.fab_upload)
            }
        }

        observeUnreadCount()
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun observeUnreadCount() {
        lifecycleScope.launch {
            notificationDao.getUnreadNotificationCount().collectLatest { count ->
                updateNotificationBadge(count)
            }
        }
    }

    private fun updateNotificationBadge(count: Int) {
        if (count > 0) {
            notifBadge.text = if (count > 99) "99+" else count.toString()
            notifBadge.visibility = View.VISIBLE
        } else {
            notifBadge.visibility = View.GONE
        }
    }

    private fun setActiveIcon(activeId: Int) {
        val inactiveColor = ContextCompat.getColor(this, android.R.color.black)
        val activeColor = ContextCompat.getColor(this, android.R.color.white)

        listOf(icHome, icSearch, icNoti, icAccount).forEach { btn ->
            btn.setColorFilter(inactiveColor)
            btn.background = null
        }

        fabUpload.setColorFilter(inactiveColor)

        val outerIv = findViewById<ImageView?>(R.id.fab_outer)

        fun restoreOuterDrawable() {
            outerIv?.let { iv ->
                val orig = ContextCompat.getDrawable(this, R.drawable.fab_outer_layer)
                iv.setImageDrawable(orig)
            }
        }

        when (activeId) {
            R.id.fab_upload -> {
                fabUpload.setColorFilter(activeColor)
                outerIv?.setImageResource(R.drawable.fab_outer_layer_active)
            }
            else -> {
                val btn = findViewById<ImageButton>(activeId)
                btn?.let {
                    it.setColorFilter(activeColor)
                    it.setBackgroundResource(R.drawable.icon_active_bg)
                }
                restoreOuterDrawable()
                fabUpload.setColorFilter(inactiveColor)
            }
        }
    }

    // Đổi thành public để ProfileFragment có thể gọi
    fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val tx = supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host, fragment)
        if (addToBackStack) tx.addToBackStack(null)
        tx.commit()

        if (bottomBarHeight > 0) {
            supportFragmentManager.findFragmentById(R.id.main_nav_host)?.view?.post {
                applyBottomPaddingToFragments()
            }
        }
    }

    private fun applyBottomPaddingToFragments() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host)
        when (currentFragment) {
            is HomeFragment -> currentFragment.view?.findViewById<View>(R.id.root_layout)?.setPadding(0, 0, 0, bottomBarHeight)
            is SearchFragment -> currentFragment.view?.findViewById<View>(R.id.root_layout)?.setPadding(0, 0, 0, bottomBarHeight)
            is UploadFragment -> currentFragment.view?.findViewById<View>(R.id.upload_scroll_view)?.setPadding(0, 0, 0, bottomBarHeight)
            is NotiFragment -> currentFragment.view?.findViewById<View>(R.id.rv_notif)?.setPadding(0, 0, 0, bottomBarHeight)
            is ProfileFragment -> currentFragment.view?.findViewById<View>(R.id.rv_docs)?.setPadding(0, 0, 0, bottomBarHeight)
            // MỚI: Padding cho Leaderboard
            is LeaderboardFragment -> currentFragment.view?.findViewById<View>(R.id.pager_leaderboard)?.setPadding(0, 0, 0, bottomBarHeight)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateActiveFromCurrentFragment()
        if (bottomBarHeight > 0) {
            supportFragmentManager.findFragmentById(R.id.main_nav_host)?.view?.post {
                applyBottomPaddingToFragments()
            }
        }
    }

    private fun updateActiveFromCurrentFragment() {
        val current = supportFragmentManager.findFragmentById(R.id.main_nav_host)
        val idToActivate = when (current) {
            is HomeFragment -> R.id.ic_home
            is SearchFragment -> R.id.ic_search
            is NotiFragment -> R.id.ic_notifications
            is ProfileFragment -> R.id.ic_profile
            is UploadFragment -> R.id.fab_upload
            // MỚI: Giữ icon Profile sáng khi đang xem Bảng xếp hạng
            is LeaderboardFragment -> R.id.ic_profile
            else -> R.id.ic_home
        }
        setActiveIcon(idToActivate)
    }
}
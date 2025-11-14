package com.stushare.feature_contribution

import com.stushare.feature_contribution.ui.upload.UploadFragment

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.stushare.feature_contribution.ui.account.ProfileFragment
import com.stushare.feature_contribution.ui.home.HomeFragment
import com.stushare.feature_contribution.ui.noti.NotiFragment
import com.stushare.feature_contribution.ui.search.SearchFragment
import androidx.core.content.ContextCompat

/**
 * MainActivity quản lý bottom bar (FAB) global. Khi FAB được click sẽ forward
 * sự kiện cho fragment hiện tại nếu fragment implement FabClickListener.
 */
class MainActivity : AppCompatActivity() {

    // Interface để các Fragment implement khi muốn nhận sự kiện FAB
    interface FabClickListener {
        fun onFabClicked()
    }

    private lateinit var fabUpload: ImageButton
    private lateinit var icHome: ImageButton
    private lateinit var icSearch: ImageButton
    private lateinit var icNoti: ImageButton
    private lateinit var icAccount: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // binding các nút bottom bar
        icHome = findViewById(R.id.ic_home)
        icSearch = findViewById(R.id.ic_search)
        icNoti = findViewById(R.id.ic_notifications)
        icAccount = findViewById(R.id.ic_profile)
        fabUpload = findViewById(R.id.fab_upload)

        // đảm bảo fab_container nổi trên cùng (nếu cần)
        findViewById<View?>(R.id.fab_container)?.bringToFront()

        // show Home lần đầu
        if (savedInstanceState == null) {
            openFragment(HomeFragment(), addToBackStack = false)
            // set Home active
            setActiveIcon(R.id.ic_home)
        } else {
            // khi activity tái tạo (rotation/backstack restored) : đồng bộ màu theo fragment hiện tại
            updateActiveFromCurrentFragment()
        }

        // click listeners và set active
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

        // Global FAB click handler:
        // - nếu fragment hiện tại implement FabClickListener: forward event (không đổi màu)
        // - nếu không: mở UploadFragment và set màu active cho FAB
        fabUpload.setOnClickListener {
            val current = supportFragmentManager.findFragmentById(R.id.main_nav_host)
            if (current is FabClickListener) {
                (current as FabClickListener).onFabClicked()
            } else {
                openFragment(UploadFragment())
                setActiveIcon(R.id.fab_upload)
            }
        }
    }

    /**
     * Đặt màu cho icon active, reset các icon khác về mặc định.
     * activeId: id của ImageButton (vd R.id.ic_home, R.id.fab_upload...)
     */
    private fun setActiveIcon(activeId: Int) {
        // màu
        val inactiveColor = ContextCompat.getColor(this, android.R.color.black)
        val activeColor = ContextCompat.getColor(this, android.R.color.white)
        val activeBgColorInt = android.graphics.Color.parseColor("#27AE60")

        // reset all non-FAB icons (remove bg + icon color)
        listOf(icHome, icSearch, icNoti, icAccount).forEach { btn ->
            btn.setColorFilter(inactiveColor)
            btn.background = null
        }

        // reset FAB icon color (ở trạng thái mặc định)
        fabUpload.setColorFilter(inactiveColor)

        // tham chiếu đến outer ImageView (vòng trắng/green) — nullable, chỉ tạo 1 lần
        val outerIv = findViewById<ImageView?>(R.id.fab_outer)

        // helper: restore original drawable for outer (nếu muốn)
        fun restoreOuterDrawable() {
            outerIv?.let { iv ->
                val orig = ContextCompat.getDrawable(this, R.drawable.fab_outer_layer)
                iv.setImageDrawable(orig)
            }
        }

        when (activeId) {
            R.id.fab_upload -> {
                // bật FAB active: icon trắng, đổi drawable vòng ngoài thành bản active
                fabUpload.setColorFilter(activeColor)
                outerIv?.setImageResource(R.drawable.fab_outer_layer_active)
            }
            else -> {
                // active icon khác: set background bo góc + restore outer về mặc định
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

    /**
     * Mở fragment vào container R.id.main_nav_host.
     */
    private fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        val tx = supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host, fragment)
        if (addToBackStack) tx.addToBackStack(null)
        tx.commit()
    }

    /**
     * Khi người dùng bấm Back, sau khi pop fragment ta cập nhật lại icon active
     */
    override fun onBackPressed() {
        super.onBackPressed()
        // Sau pop, fragment hiện tại đã thay đổi -> cập nhật icon
        updateActiveFromCurrentFragment()
    }

    /**
     * Kiểm tra fragment hiện tại và set icon tương ứng
     */
    private fun updateActiveFromCurrentFragment() {
        val current = supportFragmentManager.findFragmentById(R.id.main_nav_host)
        val idToActivate = when (current) {
            is HomeFragment -> R.id.ic_home
            is SearchFragment -> R.id.ic_search
            is NotiFragment -> R.id.ic_notifications
            is ProfileFragment -> R.id.ic_profile
            is UploadFragment -> R.id.fab_upload
            else -> R.id.ic_home
        }
        setActiveIcon(idToActivate)
    }
}

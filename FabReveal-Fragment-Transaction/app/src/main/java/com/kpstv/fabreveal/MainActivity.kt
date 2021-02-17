package com.kpstv.fabreveal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

class MainActivity : AbstractBottomNavActivity() {

    fun interface FragmentBackPressed {
        // If returned true then it means fragment has consumed the back press
        fun onBackPressed(): Boolean
    }

    override val bottomNavigationViewId: Int = R.id.bottomNavView
    override val fragmentContainerId: Int = R.id.fragment_container
    override val bottomNavFragments: MutableMap<Int, KClass<out Fragment>> = mutableMapOf(
        R.id.action_home to HomeFragment::class,
        R.id.action_watchlist to WatchFragment::class,
        R.id.action_library to LibraryFragment::class
    )

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.rootView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        window.statusBarColor = 0
        window.navigationBarColor = 0

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is FragmentBackPressed && fragment.onBackPressed()) {
            return
        }

        super.onBackPressed()
    }
}
package com.kpstv.fabreveal

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.google.android.material.floatingactionbutton.FloatingActionButton

open class BaseFragment(
    private val headline: String,
    @ColorInt val backgroundColor: Int
) : Fragment(R.layout.fragment_common) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = headline
        textView.background = ContextCompat.getDrawable(requireContext(), backgroundColor)
    }
}

class HomeFragment : BaseFragment("Home fragment", R.color.grayish_blue)
class WatchFragment : BaseFragment("Watch fragment", R.color.cement_green)

class LibraryRootFragment : BaseFragment("Library fragment", R.color.orange_red)
class LibrarySecondFragment : BaseFragment("Second fragment", R.color.yellow)

class LibraryFragment : Fragment(R.layout.fragment_third), MainActivity.FragmentBackPressed {
    private var fabReveal: FabReveal? = null

    private lateinit var fab: FloatingActionButton
    private lateinit var containerView: FragmentContainerView
    private lateinit var rootView: View

    private val frag1 = LibraryRootFragment()
    private val frag2 = LibrarySecondFragment()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab = view.findViewById(R.id.fab)
        rootView = view.findViewById(R.id.rootView)
        containerView = view.findViewById(R.id.child_fragment_container)

        fab.setOnClickListener {
            // If you have a custom background color on fragment like this example does,
            // you should set the same for the rootView to improve animation look & feel.
            rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), frag1.backgroundColor))

            fabReveal = FabReveal(fab, containerView).apply {
                start(transaction = {
                    setFragment(frag2)
                })
            }
        }

        setFragment(frag1)
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.commit {
            replace(R.id.child_fragment_container, fragment)
        }
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
        if (fragment is LibrarySecondFragment) {
            // If you have a custom background color on fragment like this example does,
            // you should set the same for the rootView to improve animation look & feel.
            rootView.setBackgroundColor(ContextCompat.getColor(requireContext(), frag2.backgroundColor))

            setFragment(LibraryRootFragment())
            fabReveal?.reverse()
            return true
        }
        return false
    }
}
package com.busytrack.openlivetrivia.generic.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.PagerAdapter

/**
 * Base PagerAdapter class, handles item instantiation and destruction details
 */
abstract class BasePagerAdapter<BINDING : ViewBinding>(
    private val pagerViewListener: PagerViewListener<BINDING>
): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = inflateLayout(container)
        container.addView(binding.root) // TODO Test
        pagerViewListener.onViewInflated(position, binding)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (`object` as View).let {
            pagerViewListener.onViewDestroyed(position)
            container.removeView(it)
        }
    }

    abstract fun inflateLayout(container: ViewGroup): BINDING
}
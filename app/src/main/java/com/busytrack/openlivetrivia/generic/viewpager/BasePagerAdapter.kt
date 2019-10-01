package com.busytrack.openlivetrivia.generic.viewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Base PagerAdapter class, handles item instantiation and destruction details
 */
abstract class BasePagerAdapter( // TODO test
    private val pagerViewListener: PagerViewListener
): PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return LayoutInflater.from(container.context)
            .inflate(
                getLayoutResId(position),
                container,
                false
            ).also {
                pagerViewListener.onViewInflated(it)
                container.addView(it)
            }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (`object` as View).let {
            pagerViewListener.onViewDestroyed(it)
            container.removeView(it)
        }
    }

    abstract fun getLayoutResId(pos: Int): Int
}
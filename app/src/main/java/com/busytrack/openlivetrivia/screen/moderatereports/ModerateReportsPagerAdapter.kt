package com.busytrack.openlivetrivia.screen.moderatereports

import android.content.Context
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.generic.viewpager.BasePagerAdapter
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener

class ModerateReportsPagerAdapter( // TODO test
    private val context: Context,
    pagerViewListener: PagerViewListener
) : BasePagerAdapter(pagerViewListener) {

    override fun getLayoutResId(pos: Int): Int {
        return when(pos) {
            0 -> R.layout.layout_tab_reported_entries
            1 -> R.layout.layout_tab_banned_entries
            else -> throw IllegalArgumentException("No layout declared for selected position ($pos)")
        }
    }

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(when(position) {
            0 -> R.string.tab_reported_entries
            1 -> R.string.tab_banned_entries
            else -> throw IllegalArgumentException("No page title declared for the selected position ($position)")
        })
    }
}
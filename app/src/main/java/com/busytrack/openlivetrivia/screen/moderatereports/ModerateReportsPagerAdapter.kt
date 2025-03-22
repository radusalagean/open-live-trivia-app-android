package com.busytrack.openlivetrivia.screen.moderatereports

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.busytrack.openlivetrivia.R
import com.busytrack.openlivetrivia.databinding.LayoutTabModerateReportsBinding
import com.busytrack.openlivetrivia.generic.viewpager.BasePagerAdapter
import com.busytrack.openlivetrivia.generic.viewpager.PagerViewListener

class ModerateReportsPagerAdapter(
    private val context: Context,
    pagerViewListener: PagerViewListener<LayoutTabModerateReportsBinding>
) : BasePagerAdapter<LayoutTabModerateReportsBinding>(pagerViewListener) {

    override fun getCount() = 2

    override fun getPageTitle(position: Int): CharSequence {
        return context.getString(when(position) {
            INDEX_TAB_REPORTED_ENTRIES -> R.string.tab_reported_entries
            INDEX_TAB_BANNED_ENTRIES -> R.string.tab_banned_entries
            else -> throw IllegalArgumentException("No page title declared for the selected position ($position)")
        })
    }

    override fun inflateLayout(container: ViewGroup): LayoutTabModerateReportsBinding {
        return LayoutTabModerateReportsBinding.inflate(
            LayoutInflater.from(context), container, false
        )
    }

    companion object {
        const val INDEX_TAB_REPORTED_ENTRIES = 0
        const val INDEX_TAB_BANNED_ENTRIES = 1
    }
}
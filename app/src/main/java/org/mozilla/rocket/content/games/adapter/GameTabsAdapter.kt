package org.mozilla.rocket.content.games.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.mozilla.focus.R
import org.mozilla.rocket.content.games.BrowserGamesFragment
import org.mozilla.rocket.content.games.GamesActivity

class GameTabsAdapter(
    fm: FragmentManager,
    activity: FragmentActivity,
    private val items: List<TabItem> = DEFAULT_TABS
) : FragmentPagerAdapter(fm) {

    private val resource = activity.resources

    override fun getItem(position: Int): Fragment = items[position].fragment

    override fun getCount(): Int = items.size

    override fun getPageTitle(position: Int): CharSequence? = resource.getString(items[position].titleResId)

    data class TabItem(
        val fragment: Fragment,
        val titleResId: Int
    )

    companion object {
        private val DEFAULT_TABS: List<TabItem> by lazy {
            listOf(
                TabItem(BrowserGamesFragment(), R.string.mozilla),
                TabItem(GamesActivity.PremiumGamesFragment(), R.string.firefox)
            )
        }
    }
}
/* -*- Mode: Java; c-basic-offset: 4; tab-width: 20; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Context
import android.os.StrictMode
import android.preference.PreferenceManager
import android.support.test.InstrumentationRegistry
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.animation.Animation
import org.mozilla.focus.persistence.TabsDatabase
import org.mozilla.focus.utils.AppConstants
import org.mozilla.focus.utils.RemoteConfigConstants
import org.mozilla.rocket.download.DownloadIndicatorViewModel
import org.mozilla.rocket.download.DownloadInfoRepository
import org.mozilla.rocket.download.DownloadInfoViewModel
import org.mozilla.rocket.download.DownloadViewModelFactory
import org.mozilla.rocket.urlinput.GlobalDataSource
import org.mozilla.rocket.urlinput.LocaleDataSource
import org.mozilla.rocket.urlinput.QuickSearchRepository
import org.mozilla.rocket.urlinput.QuickSearchViewModel
import org.mozilla.rocket.urlinput.QuickSearchViewModelFactory

object Inject {

    @JvmStatic
    val activityNewlyCreatedFlag = true

    private val TOP_SITES =
        "[{\"id\":-1,\"url\":\"file:\\/\\/\\/android_asset\\/gpl.html\",\"title\":\"Sample Top Site\",\"favicon\":\"ic_youtube.png\",\"viewCount\":20,\"lastViewTimestamp\":1517196818119},{\"id\":-3,\"url\":\"http:\\/\\/m.tribunnews.com\\/\",\"title\":\"Tribunnews\",\"favicon\":\"ic_tribunnews.png\",\"viewCount\":18,\"lastViewTimestamp\":1517196818119},{\"id\":-5,\"url\":\"https:\\/\\/m.tokopedia.com\\/\",\"title\":\"Tokopedia\",\"favicon\":\"ic_tokopedia.png\",\"viewCount\":16,\"lastViewTimestamp\":1517196818119},{\"id\":-4,\"url\":\"https:\\/\\/m.facebook.com\\/\",\"title\":\"Facebook\",\"favicon\":\"ic_facebook.png\",\"viewCount\":14,\"lastViewTimestamp\":1517196818119},{\"id\":-8,\"url\":\"https:\\/\\/m.bukalapak.com\\/\",\"title\":\"Bukalapak\",\"favicon\":\"ic_bukalapak.png\",\"viewCount\":12,\"lastViewTimestamp\":1517196818119},{\"id\":-6,\"url\":\"http:\\/\\/m.liputan6.com\\/\",\"title\":\"Liputan6\",\"favicon\":\"ic_liputan6.png\",\"viewCount\":10,\"lastViewTimestamp\":1517196818119},{\"id\":-7,\"url\":\"http:\\/\\/www.kompas.com\\/\",\"title\":\"Kompas\",\"favicon\":\"ic_kompas.png\",\"viewCount\":8,\"lastViewTimestamp\":1517196818119},{\"id\":-9,\"url\":\"https:\\/\\/m.kapanlagi.com\\/\",\"title\":\"Kapanlagi\",\"favicon\":\"ic_kapanlagi.png\",\"viewCount\":6,\"lastViewTimestamp\":1517196818119}]"

    @Volatile
    private var tabsDatabase: TabsDatabase? = null

    @JvmStatic
    val isUnderEspressoTest: Boolean
        get() = true

    val defaultFeatureSurvey: RemoteConfigConstants.SURVEY
        get() = RemoteConfigConstants.SURVEY.VPN_RECOMMENDER

    @JvmStatic
    fun getDefaultTopSites(context: Context): String {
        return TOP_SITES
    }

    @JvmStatic
    fun getTabsDatabase(context: Context): TabsDatabase? {
        if (tabsDatabase == null || !tabsDatabase!!.isOpen) {
            synchronized(Inject::class.java) {
                if (tabsDatabase == null || !tabsDatabase!!.isOpen) {
                    // using an in-memory database because the information stored here disappears
                    // when the process is killed
                    tabsDatabase =
                        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), TabsDatabase::class.java)
                            .build()
                }
            }
        }
        return tabsDatabase
    }

    // The pref is not persist so we need to inject the condition instead of override defaultSharedPreference
    fun isTelemetryEnabled(context: Context): Boolean {
        // The first access to shared preferences will require a disk read.
        val threadPolicy = StrictMode.allowThreadDiskReads()
        try {
            val resources = context.resources
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(resources.getString(R.string.pref_key_telemetry), true)
        } finally {
            StrictMode.setThreadPolicy(threadPolicy)
        }
    }

    fun enableStrictMode() {
        if (AppConstants.isReleaseBuild()) {
            return
        }

        val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder().detectAll()
        val vmPolicyBuilder = StrictMode.VmPolicy.Builder().detectAll()

        // In AndroidTest we are super kind :)
        threadPolicyBuilder.penaltyLog()
        // Previously we have penaltyDeath() for debug build, but in order to add crashlytics, we can't use it here.
        // ( crashlytics has untagged Network violation so it always crashes
        vmPolicyBuilder.penaltyLog()

        StrictMode.setThreadPolicy(threadPolicyBuilder.build())
        StrictMode.setVmPolicy(vmPolicyBuilder.build())
    }

    @JvmStatic
    fun setActivityNewlyCreatedFlag() {
        // Do nothing in testing
    }

    fun provideDownloadInfoRepository(): DownloadInfoRepository? {
        // TODO inject data source, ex production DB or mock DB here
        return DownloadInfoRepository.getInstance()
    }

    @JvmStatic
    fun obtainDownloadIndicatorViewModel(activity: FragmentActivity): DownloadIndicatorViewModel {
        val factory = DownloadViewModelFactory.getInstance()
        return ViewModelProviders.of(activity, factory).get(DownloadIndicatorViewModel::class.java)
    }

    @JvmStatic
    fun obtainDownloadInfoViewModel(activity: FragmentActivity): DownloadInfoViewModel {
        val factory = DownloadViewModelFactory.getInstance()
        return ViewModelProviders.of(activity, factory).get(DownloadInfoViewModel::class.java)
    }

    private fun provideQuickSearchRepository(context: Context): QuickSearchRepository? {
        // TODO add mock data source
        return QuickSearchRepository.getInstance(
            GlobalDataSource.getInstance(context)!!,
            LocaleDataSource.getInstance(context)!!
        )
    }

    fun obtainQuickSearchViewModel(activity: FragmentActivity): QuickSearchViewModel {
        val factory = QuickSearchViewModelFactory(provideQuickSearchRepository(activity.applicationContext)!!)
        return ViewModelProviders.of(activity, factory).get(QuickSearchViewModel::class.java)
    }

    @JvmStatic
    fun startAnimation(view: View, animation: Animation) {}
}

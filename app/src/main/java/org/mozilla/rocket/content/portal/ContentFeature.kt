package org.mozilla.rocket.content.portal

import org.mozilla.focus.utils.AppConfigWrapper
import org.mozilla.focus.utils.Settings

/**
 * Storing the logic of how we provide content portal features
 *
 * */
class ContentFeature(val settings: Settings) {

    companion object {
        const val TYPE_NEWS = 1 shl 0
        const val TYPE_TICKET = 1 shl 1
        const val TYPE_COUPON = 1 shl 2
        const val TYPE_KEY = "contentType"
        const val EXTRA_CONFIG_NEWS = "extra_config_news"
        const val EXTRA_NEWS_LANGUAGE = "extra_news_language"
        const val SETTING_REQUEST_CODE = 1492

        private const val DEFAULT = "0"
        private const val FORCE_ENABLE_NEWS = "1"
        private const val FORCE_ENABLE_E_COMMERCE = "2"
    }

    fun hasNews() = (AppConfigWrapper.hasNewsPortal() && settings.lifeFeedSettings == DEFAULT) ||
        (settings.lifeFeedSettings == FORCE_ENABLE_NEWS)

    fun hasCoupon() = (AppConfigWrapper.hasEcommerceCoupons() && settings.lifeFeedSettings == DEFAULT) ||
        (settings.lifeFeedSettings == FORCE_ENABLE_E_COMMERCE)

    fun hasShoppingLink() = (AppConfigWrapper.hasEcommerceShoppingLink() && settings.lifeFeedSettings == DEFAULT) ||
        (settings.lifeFeedSettings == FORCE_ENABLE_E_COMMERCE)

    fun hasContentPortal() = hasNews() || (hasCoupon() && hasShoppingLink())

    // get the eCommerceFeatures from remote config
    // if we want to change the order of tabs we should do it here.
    fun eCommerceFeatures(): ArrayList<Int> {

        val features = ArrayList<Int>()
        if (hasShoppingLink()) {
            features.add(TYPE_TICKET)
        }

        if (hasCoupon()) {
            features.add(TYPE_COUPON)
        }

        return features
    }
}

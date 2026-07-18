package com.multiplatform.webview.cookie

import kotlinx.browser.document
import kotlin.js.Date

internal actual fun jsDateToUTCString(timestamp: Long): String = Date(timestamp.toDouble()).toUTCString()

internal actual fun setJsCookie(cookieStr: String) {
    document.cookie = cookieStr
}

internal actual fun getJsCookies(): String = document.cookie

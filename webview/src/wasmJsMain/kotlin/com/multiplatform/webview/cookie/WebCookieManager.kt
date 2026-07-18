package com.multiplatform.webview.cookie

@JsFun("timestamp => new Date(timestamp).toUTCString()")
private external fun jsDateToUTCStringImpl(timestamp: Double): String

internal actual fun jsDateToUTCString(timestamp: Long): String = jsDateToUTCStringImpl(timestamp.toDouble())

@JsFun("cookieStr => { document.cookie = cookieStr; }")
private external fun setJsCookieImpl(cookieStr: String)

internal actual fun setJsCookie(cookieStr: String) = setJsCookieImpl(cookieStr)

@JsFun("() => document.cookie")
private external fun getJsCookiesImpl(): String

internal actual fun getJsCookies(): String = getJsCookiesImpl()

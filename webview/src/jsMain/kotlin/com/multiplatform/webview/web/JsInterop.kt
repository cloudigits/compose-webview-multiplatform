package com.multiplatform.webview.web

import org.w3c.dom.Element

/**
 * Check if navigation back is possible
 */
actual fun checkCanGoBackJs(element: Element): Boolean =
    try {
        val contentWindow = element.asDynamic().contentWindow
        contentWindow != null &&
            contentWindow.history != null &&
            (contentWindow.history.length as Int) > 1
    } catch (_: Throwable) {
        false
    }

/**
 * Check if navigation forward is possible (always returns false for iframes)
 */
actual fun checkCanGoForwardJs(element: Element): Boolean = false

/**
 * Navigate back in browser history
 */
actual fun navigateBackJs(element: Element) {
    try {
        val contentWindow = element.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.history != null) {
            contentWindow.history.back()
        }
    } catch (e: Throwable) {
        console.error("Error going back:", e)
    }
}

/**
 * Navigate forward in browser history
 */
actual fun navigateForwardJs(element: Element) {
    try {
        val contentWindow = element.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.history != null) {
            contentWindow.history.forward()
        }
    } catch (e: Throwable) {
        console.error("Error going forward:", e)
    }
}

/**
 * Reload the current page
 */
actual fun reloadJs(element: Element) {
    try {
        val contentWindow = element.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.location != null) {
            contentWindow.location.reload()
        }
    } catch (e: Throwable) {
        console.error("Error reloading:", e)
    }
}

/**
 * Stop loading the current page
 */
actual fun stopLoadingJs(element: Element) {
    try {
        val contentWindow = element.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.stop != null) {
            contentWindow.stop()
        }
    } catch (e: Throwable) {
        console.error("Error stopping load:", e)
    }
}

/**
 * Set the URL of an iframe
 */
actual fun setUrlJs(
    element: Element,
    url: String,
) {
    try {
        element.asDynamic().src = url
    } catch (e: Throwable) {
        console.error("Error setting URL:", e)
    }
}

/**
 * Set HTML content of an iframe via srcdoc attribute
 */
actual fun setHtmlContentJs(
    element: Element,
    content: String,
) {
    try {
        element.asDynamic().srcdoc = content
    } catch (e: Throwable) {
        console.error("Error setting HTML:", e)
    }
}

/**
 * Evaluate JavaScript in the iframe context
 */
actual fun evaluateScriptJs(
    element: Element,
    script: String,
): String =
    try {
        val contentWindow = element.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.eval != null) {
            contentWindow.eval(script).toString()
        } else {
            ""
        }
    } catch (err: Throwable) {
        "Error: ${err.message}"
    }

/**
 * Get the title from an iframe document
 */
actual fun getIframeTitleJs(iframe: Element): String? =
    try {
        val contentDocument = iframe.asDynamic().contentDocument
        if (contentDocument != null) contentDocument.title as String else null
    } catch (_: Throwable) {
        null
    }

/**
 * Get the current URL from an iframe
 */
actual fun getIframeUrlJs(iframe: Element): String? =
    try {
        val contentWindow = iframe.asDynamic().contentWindow
        if (contentWindow != null && contentWindow.location != null) {
            contentWindow.location.href as String
        } else {
            null
        }
    } catch (_: Throwable) {
        iframe.asDynamic().src as? String
    }

/**
 * Set a style property on an element
 */
actual fun setStyleJs(
    element: Element,
    property: String,
    value: String,
) {
    element.asDynamic().style[property] = value
}

/**
 * Add a content identifier to an iframe for history tracking
 */
actual fun addContentIdentifierJs(iframe: Element) {
    try {
        val contentWindow = iframe.asDynamic().contentWindow
        if (contentWindow != null) {
            val uniqueId = kotlin.random.Random.nextDouble().toString().substring(2)
            val state: dynamic = js("({})")
            state.id = uniqueId
            contentWindow.history.replaceState(state, "", contentWindow.location.href)
        }
    } catch (e: Throwable) {
        console.error("Error adding content identifier:", e)
    }
}

/**
 * Log a message to the console
 */
actual fun consoleLogJs(message: String) {
    console.log(message)
}

/**
 * Log an info message to the console
 */
actual fun consoleInfoJs(message: String) {
    console.info(message)
}

/**
 * Log an error message to the console
 */
actual fun consoleErrorJs(message: String) {
    console.error(message)
}

/**
 * Position an element with coordinates
 */
actual fun changeCoordinates(
    element: Element,
    width: Float,
    height: Float,
    x: Float,
    y: Float,
) {
    val style = element.asDynamic().style
    style.width = "${width}px"
    style.height = "${height}px"
    style.left = "${x}px"
    style.top = "${y}px"
}

/**
 * Initialize an element with basic styling
 */
actual fun initializingElement(element: Element) {
    val style = element.asDynamic().style
    style.position = "absolute"
    style.margin = "0px"
}

/**
 * Request focus on an element
 */
actual fun requestFocus(element: Element) {
    element.asDynamic().focus()
}

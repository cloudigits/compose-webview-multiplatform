package com.multiplatform.webview.web

import org.w3c.dom.Element

/**
 * Check if navigation back is possible
 */
expect fun checkCanGoBackJs(element: Element): Boolean

/**
 * Check if navigation forward is possible (always returns false for iframes)
 */
expect fun checkCanGoForwardJs(element: Element): Boolean

/**
 * Navigate back in browser history
 */
expect fun navigateBackJs(element: Element)

/**
 * Navigate forward in browser history
 */
expect fun navigateForwardJs(element: Element)

/**
 * Reload the current page
 */
expect fun reloadJs(element: Element)

/**
 * Stop loading the current page
 */
expect fun stopLoadingJs(element: Element)

/**
 * Set the URL of an iframe
 */
expect fun setUrlJs(
    element: Element,
    url: String,
)

/**
 * Set HTML content of an iframe via srcdoc attribute
 */
expect fun setHtmlContentJs(
    element: Element,
    content: String,
)

/**
 * Evaluate JavaScript in the iframe context
 */
expect fun evaluateScriptJs(
    element: Element,
    script: String,
): String

/**
 * Get the title from an iframe document
 */
expect fun getIframeTitleJs(iframe: Element): String?

/**
 * Get the current URL from an iframe
 */
expect fun getIframeUrlJs(iframe: Element): String?

/**
 * Set a style property on an element
 */
expect fun setStyleJs(
    element: Element,
    property: String,
    value: String,
)

/**
 * Add a content identifier to an iframe for history tracking
 */
expect fun addContentIdentifierJs(iframe: Element)

/**
 * Log a message to the console
 */
expect fun consoleLogJs(message: String)

/**
 * Log an info message to the console
 */
expect fun consoleInfoJs(message: String)

/**
 * Log an error message to the console
 */
expect fun consoleErrorJs(message: String)

/**
 * Position an element with coordinates
 */
expect fun changeCoordinates(
    element: Element,
    width: Float,
    height: Float,
    x: Float,
    y: Float,
)

/**
 * Initialize an element with basic styling
 */
expect fun initializingElement(element: Element)

/**
 * Request focus on an element
 */
expect fun requestFocus(element: Element)

package com.multiplatform.webview.web

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.viewinterop.HtmlElementView
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLIFrameElement
import org.w3c.dom.events.Event

/**
 * A Composable that renders HTML content using an iframe
 *
 * @param state The state of the HTML view
 * @param modifier The modifier for this composable
 * @param navigator The navigator for HTML navigation events
 * @param onCreated Callback invoked when the view is created
 * @param onDispose Callback invoked when the view is disposed
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HtmlView(
    state: HtmlViewState,
    modifier: Modifier = Modifier,
    navigator: HtmlViewNavigator = rememberHtmlViewNavigator(),
    onCreated: (Element) -> Unit = {},
    onDispose: (Element) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val iframe =
        remember {
            (document.createElement("iframe") as HTMLIFrameElement).apply {
                style.border = "none"
            }
        }

    DisposableEffect(iframe) {
        val loadCallback: (Event) -> Unit = {
            state.loadingState = HtmlLoadingState.Finished()

            try {
                val title = getIframeTitleJs(iframe)
                if (title != null) {
                    state.pageTitle = title
                }

                val href = getIframeUrlJs(iframe)
                if (href != null && href != "about:blank") {
                    state.lastLoadedUrl = href
                }
            } catch (e: Exception) {
                // Cross-origin restrictions might prevent access
            }
        }

        val errorCallback: (Event) -> Unit = {
            state.loadingState =
                HtmlLoadingState.Finished(
                    isError = true,
                    errorMessage = "Failed to load content",
                )
        }

        iframe.addEventListener("load", loadCallback)
        iframe.addEventListener("error", errorCallback)
        state.htmlElement = iframe
        onCreated(iframe)

        onDispose {
            iframe.removeEventListener("load", loadCallback)
            iframe.removeEventListener("error", errorCallback)
            onDispose(iframe)
            state.htmlElement = null
            state.loadingState = HtmlLoadingState.Initializing
        }
    }

    LaunchedEffect(iframe, navigator) {
        navigator.handleNavigationEvents(iframe)
    }

    HtmlElementView(
        factory = { iframe },
        modifier =
            modifier
                .onFocusChanged {
                    if (it.isFocused) {
                        focusManager.clearFocus(force = true)
                        iframe.focus()
                    }
                }.focusTarget(),
        update = {
            when (val content = state.content) {
                is HtmlContent.Url -> {
                    setUrlJs(iframe, content.url)
                    state.loadingState = HtmlLoadingState.Loading
                }

                is HtmlContent.Data -> {
                    setHtmlContentJs(iframe, content.data)
                    state.loadingState = HtmlLoadingState.Loading
                    addContentIdentifierJs(iframe)
                }

                is HtmlContent.Post -> {
                    // POST requests not directly supported in iframe
                }

                HtmlContent.NavigatorOnly -> {
                    // No content update needed
                }
            }
        },
    )
}

/**
 * Composable for displaying a URL in an HtmlView
 */
@Composable
fun HtmlViewUrl(
    url: String,
    modifier: Modifier = Modifier,
    headers: Map<String, String> = emptyMap(),
    navigator: HtmlViewNavigator = rememberHtmlViewNavigator(),
) {
    val state = rememberHtmlViewState()

    LaunchedEffect(url, headers) {
        state.content = HtmlContent.Url(url, headers)
    }

    HtmlView(
        state = state,
        modifier = modifier,
        navigator = navigator,
        onCreated = {},
        onDispose = {},
    )
}

/**
 * Composable for displaying HTML content in an HtmlView
 */
@Composable
fun HtmlViewContent(
    htmlContent: String,
    modifier: Modifier = Modifier,
    baseUrl: String? = null,
    navigator: HtmlViewNavigator = rememberHtmlViewNavigator(),
) {
    val state = rememberHtmlViewState()

    LaunchedEffect(htmlContent, baseUrl) {
        state.content = HtmlContent.Data(htmlContent, baseUrl)
    }

    HtmlView(
        state = state,
        modifier = modifier,
        navigator = navigator,
        onCreated = {},
        onDispose = {},
    )
}

/**
 * Create and remember an HtmlViewState instance
 */
@Composable
fun rememberHtmlViewState(): HtmlViewState = remember { HtmlViewState() }

package edu.temple.browsr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import java.net.URLEncoder

class PageFragment : Fragment() {
    private var webView: WebView? = null
    private var lastUrl: String? = null
    private var urlUpdateListener: UrlUpdateListener? = null

    interface UrlUpdateListener {
        fun onUrlUpdate(url: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView)
        setupWebView()

        // Restore last URL if available
        savedInstanceState?.getString("lastUrl")?.let { url ->
            loadUrl(url)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("lastUrl", lastUrl)
    }

    private fun setupWebView() {
        webView?.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    url?.let {
                        lastUrl = it
                        urlUpdateListener?.onUrlUpdate(it)
                    }
                }
            }
        }
    }

    fun loadUrl(url: String) {
        val processedUrl = processUrl(url)
        webView?.loadUrl(processedUrl)
        lastUrl = processedUrl
    }

    private fun processUrl(url: String): String {
        return when {
            url.startsWith("http://") || url.startsWith("https://") -> url
            url.contains(".") && url.split(".").last().matches(Regex("[a-zA-Z]+")) ->
                "https://$url"
            else -> "https://duckduckgo.com/?q=${URLEncoder.encode(url, "UTF-8")}"
        }
    }

    fun canGoBack(): Boolean = webView?.canGoBack() == true
    fun canGoForward(): Boolean = webView?.canGoForward() == true
    fun goBack() = webView?.goBack()
    fun goForward() = webView?.goForward()

    fun setUrlUpdateListener(listener: UrlUpdateListener) {
        urlUpdateListener = listener
    }
}
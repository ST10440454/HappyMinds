package com.happyminds.app.ui.profile

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.happyminds.app.data.WORKER_URL

/**
 * Shows PayFast's secure card payment page INSIDE the app (Visa / Mastercard), so the donor types their card details
 * without leaving HappyMinds. The card fields belong to PayFast's page: the app never sees,
 * stores or handles the card number or CVC (which keeps the app out of PCI scope).
 *
 * PayFast sends the browser to <worker>/thanks or <worker>/cancelled when it is finished;
 * this screen spots those addresses and closes with RESULT_OK / RESULT_CANCELED.
 * The final result still comes from the Firestore record that the Worker updates.
 */
class PayFastCheckoutActivity : AppCompatActivity() {

    companion object { const val EXTRA_URL = "payfast_url" }

    private lateinit var webView: WebView
    private lateinit var progress: ProgressBar
    private var done = false

    @SuppressLint("SetJavaScriptEnabled")   // PayFast's payment page needs JavaScript
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(EXTRA_URL)
        if (url == null || !url.startsWith("https://")) { finish(); return }

        // The card form is on screen: block screenshots and the recent-apps thumbnail.
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

        val cancelBar = TextView(this).apply {
            text = "✕  Cancel payment"
            textSize = 16f
            setTextColor(Color.parseColor("#333333"))
            setPadding(dp(16), dp(14), dp(16), dp(14))
            setOnClickListener { finishWith(RESULT_CANCELED) }
        }
        progress = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
            max = 100
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(3))
        }
        webView = WebView(this)

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.WHITE)
            addView(cancelBar)
            addView(progress)
            addView(webView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        })

        // Banks' 3-D Secure pages (the card confirmation step) can need third-party cookies.
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = false
            allowContentAccess = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            setSupportMultipleWindows(false)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (isFinishUrl(request.url.toString())) return true
                return request.url.scheme != "https"        // never open anything that is not https
            }

            override fun onPageStarted(view: WebView, pageUrl: String, favicon: Bitmap?) {
                if (isFinishUrl(pageUrl)) return
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView, pageUrl: String) {
                progress.visibility = View.GONE
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) { progress.progress = newProgress }
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) webView.goBack() else finishWith(RESULT_CANCELED)
        }

        if (savedInstanceState != null) webView.restoreState(savedInstanceState) else webView.loadUrl(url)
    }

    /** True (and closes the screen) if PayFast has sent the browser back to our Worker. */
    private fun isFinishUrl(url: String): Boolean = when {
        url.startsWith("$WORKER_URL/thanks") -> { finishWith(RESULT_OK); true }
        url.startsWith("$WORKER_URL/cancelled") -> { finishWith(RESULT_CANCELED); true }
        else -> false
    }

    private fun finishWith(code: Int) {
        if (done) return
        done = true
        setResult(code)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    override fun onDestroy() {
        (webView.parent as? ViewGroup)?.removeView(webView)
        webView.destroy()
        super.onDestroy()
    }
}

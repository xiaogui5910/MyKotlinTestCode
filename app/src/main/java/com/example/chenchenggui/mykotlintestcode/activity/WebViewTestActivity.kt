package com.example.chenchenggui.mykotlintestcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.chenchenggui.mykotlintestcode.MyApplication
import com.example.chenchenggui.mykotlintestcode.R
import kotlinx.android.synthetic.main.activity_section.*
import kotlinx.android.synthetic.main.activity_web_view_test.*

class WebViewTestActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_web_view_test

    override fun initView() {
        initWebView()
    }

    private fun initWebView() {
        val webView = findViewById<WebView>(R.id.webView)

        webView.settings.javaScriptEnabled = true

        //js调用android方法1
        webView.addJavascriptInterface(AndroidtoJs(), "test")
        webView.loadUrl(" file:///android_asset/web/webview_test.html")

        //js调用android方法2
        webView.webViewClient = MyWebViewClient()
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val uri = Uri.parse(url)
            if (uri.scheme.equals("js")) {
                if (uri.authority.equals("gotoMain")) {
                    MyApplication.instance?.startActivity(Intent(MyApplication.instance, MainActivity::class
                            .java))
                    finish()
                }
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

    }

    class AndroidtoJs {
        @JavascriptInterface
        fun gotoDirectory(msg: String) {
            Toast.makeText(MyApplication.instance, msg, Toast.LENGTH_SHORT).show()

        }
    }
}

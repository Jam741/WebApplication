package com.android.web.webapplication

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    companion object {

        fun start(context: Context){
            context.startActivity(Intent(context,MainActivity::class.java))
        }
    }

    private val webView by lazy { WebView(this.applicationContext) }

    private val webSettings by lazy { webView.settings }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_main)
        initViews()

        initWeb()
    }


    override fun onPause() {
        if (isFinishing)fullReleaseWebView(webView)
        super.onPause()
    }


    private fun initWeb() {

        webSettings.run {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true //支持H5 DOM Storage
            setAppCacheEnabled(true) //应用缓存API是否可用
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            builtInZoomControls = true //是否使用内置的缩放机制。
            setSupportZoom(true) //WebView是否支持使用屏幕上的缩放控件和手势进行缩放，默认值true。
            displayZoomControls = false //使用内置的缩放机制时是否展示缩放控件，默认值true。

            /*自身适应屏幕尺寸*/
            loadWithOverviewMode = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            useWideViewPort = true
        }

        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }




        webView.run {
            webChromeClient = this@MainActivity.webChromeClient
            loadUrl(Constant.MAIN_URL)
        }
    }

    private fun initViews() {
        web_contrainer.addView(webView,FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
    }



    private val webChromeClient:WebChromeClient by lazy { object :WebChromeClient(){


        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            if (newProgress < 80){
                if (progressBar.visibility == View.GONE) progressBar.visibility = View.VISIBLE
                progressBar.progress = newProgress
            }else{
                progressBar.visibility = View.GONE
                progressBar.progress = 0
            }
        }


    }
    }

    /**
     * 完全释放WebView的内存，完全解决WebView 内存泄漏问题，兼容Android 5.0
     */
    private fun fullReleaseWebView(webView: WebView?) {
        if (webView != null) {

            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            if (webView.parent != null) {
                val parent = webView.parent as ViewGroup
                parent?.removeView(webView)
                webView.stopLoading()
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                webView.settings.javaScriptEnabled = false
                webView.clearHistory()
                webView.clearView()
                webView.removeAllViews()
                webView.destroy()
            }


        }
    }
}

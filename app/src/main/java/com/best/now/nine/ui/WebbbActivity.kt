package com.best.now.nine.ui

import android.app.Activity
import android.content.Intent
import android.webkit.WebViewClient
import com.best.now.nine.BaseVMActivity
import com.best.now.nine.R
import com.best.now.nine.databinding.ActivityWebBinding


/*** 选择服务界面 */
class WebbbActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebBinding>(R.layout.activity_web)
    companion object {
        fun startActivity(activity: Activity, title: String, url: String) {
            activity.startActivity(
                Intent(activity, WebbbActivity::class.java)
                    .putExtra("Title", title).putExtra("Url", url)
            )
        }
    }

    override fun initView() {
        binding.apply {
            toolBar.title = intent.getStringExtra("Title")
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
//            webView.settings.apply {
//                javaScriptEnabled = true
//                useWideViewPort = true
//                loadWithOverviewMode = true
//                userAgentString = "User-Agent:Android"
//            }
            webView.webViewClient = WebViewClient()
        }
    }

    override fun initData() {
        val url = intent.getStringExtra("Url")
        url?.let {
            binding.webView.loadUrl(it)
        }
    }


}
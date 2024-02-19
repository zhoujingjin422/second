package com.best.now.autoclick.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.best.now.autoclick.R
import com.best.now.autoclick.bean.JsonCallback
import com.best.now.autoclick.bean.UrlBean
import com.best.now.autoclick.databinding.FragmentWebBinding
import com.best.now.autoclick.utils.Constant
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.TimeUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.AbsCallback
import com.lzy.okgo.callback.Callback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.base.Request

class WebFragment:Fragment() {
    private var binding:FragmentWebBinding? = null
    companion object {
        //获取一个WebFragment的实例
        fun getInstance(): WebFragment {
            return WebFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_web,null)
         binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            webView.settings.apply {
                //webview的设置
                useWideViewPort = true
                loadWithOverviewMode = true
                domStorageEnabled = true
                defaultTextEncodingName = "UTF-8"
                allowFileAccess = true
                allowContentAccess = true
                allowFileAccessFromFileURLs = false
                allowUniversalAccessFromFileURLs = false
                javaScriptEnabled = true
            }
            webView.addJavascriptInterface(
                WebPlayPianoActivity.JavaScriptObject(requireActivity()),
                "android"
            )
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = object : WebChromeClient(){}
        }
        getUrl()
    }
    private fun getUrl(){
        OkGo.get<UrlBean>("http://haosi.ayhhhrk.cn/api/english-landing") // 请求方式和请求url
            .execute(object: JsonCallback<UrlBean>(){
                override fun onSuccess(response: Response<UrlBean>?) {
                    response?.let {
                        if (it.isSuccessful){
                            if(!it.body().data.isNullOrEmpty()){
                                binding?.webView?.loadUrl(it.body().data[0].url)
                            }
                        }
                    }
                }
            })
    }
}
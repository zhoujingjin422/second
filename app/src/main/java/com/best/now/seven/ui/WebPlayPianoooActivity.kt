package com.best.now.seven.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import androidx.core.content.FileProvider
import com.best.now.seven.BaseVMActivity
import com.best.now.seven.BuildConfig
import com.best.now.seven.R
import com.best.now.seven.bean.ShareBean
import com.best.now.seven.databinding.ActivityWebPlayPianoBinding
import com.best.now.seven.utils.ApkDownloadUtils
import com.best.now.seven.utils.Constant
import com.google.gson.Gson
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.util.regex.Pattern


/*** 选择服务界面 */
class WebPlayPianoooActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebPlayPianoBinding>(R.layout.activity_web_play_piano)
    companion object {
        fun startActivity(activity: Activity, title: String, url: String) {
            activity.startActivity(
                Intent(activity, WebPlayPianoooActivity::class.java)
                    .putExtra("Title", title).putExtra("Url", url)
            )
        }
    }


    @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
    override fun initView() {
//        val uri = intent.data
        binding.apply {
//            toolBar.title = intent.getStringExtra("Title")
//            setSupportActionBar(toolBar)
//            toolBar.setNavigationOnClickListener {
//                onBackPressed()
//            }
            webView.settings.apply {
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
            webView.addJavascriptInterface(JavaScriptObject(this@WebPlayPianoooActivity),"android")
            webView.webViewClient = WebViewClient()
            webView.webChromeClient = object : WebChromeClient(){
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    uploadMessageAboveL = filePathCallback
                    showChooseWay(fileChooserParams?.acceptTypes)
                    return true
                }
            }
        }
    }
    private var mImageUri: Uri? = null
    private fun showChooseWay(acceptTypes: Array<String>?) {
        var type = 0
        type = if (acceptTypes?.asList()?.contains(".pdf")==true){
            0
        }else if(acceptTypes?.asList()?.contains(".doc")==true){
            1
        }else{
            2
        }
        chooseFile(type)
//        if (type==0){
//        }else{
//            val pop = ChooseWayPop(this@WebPlayPianoActivity, { chooseFile(type) },{takePic()}){
//                uploadMessageAboveL?.onReceiveValue(null)
//                uploadMessageAboveL = null
//            }
//            pop.showPopupWindow()
//        }
    }
    private fun chooseFile(isPDF:Int) {
        // 创建一个Intent来打开文件浏览器并选择PDF文件

        // 创建一个Intent来打开文件浏览器并选择PDF文件
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        when (isPDF) {
            0 -> {
                intent.type = "application/pdf"
            }
            1 -> { //word + excel + ppt + image
                intent.type = "*/*"
                intent.putExtra(
                    Intent.EXTRA_MIME_TYPES, arrayOf(
                        "application/vnd.ms-word",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "image/*"
                    )
                )
            }
            2 -> {
                intent.type = "*/*"
                intent.putExtra(
                    Intent.EXTRA_MIME_TYPES, arrayOf(
                        "application/pdf",
                        "application/vnd.ms-word",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-excel",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                        "image/*"
                    )
                )
            }
        }
        // 启动文件浏览器
        startActivityForResult(intent, 101)
//        val i = Intent(Intent.ACTION_GET_CONTENT)
//        i.addCategory(Intent.CATEGORY_OPENABLE)
//        i.type = "*/*"
//        try {
//            startActivityForResult(Intent.createChooser(i, "test"), 101)
//        } catch (e: android.content.ActivityNotFoundException) {
//            ToastUtils.showShort("File management app not found, please install file management app and try again")
        }
    /**
     * 拍照
     */
    fun takePic() {
        try {
            val state = Environment.getExternalStorageState()
            if (state == Environment.MEDIA_MOUNTED) {
                val cameraIntent = Intent()
               var mFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "temp.jpg"
                )
                ///storage/emulated/0/Pictures/temp.jpg
                mImageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    createImageUri()
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //content://com.example.shinelon.takephotodemo.provider/camera_photos/Pictures/temp.jpg
                    FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, mFile)
                } else {
                    Uri.fromFile(mFile)
                }
                //指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                cameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

//                cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(cameraIntent, 112)
            }/* else {
                val toast = Toast.makeText(this, "请确认已经插入SD卡", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }*/
        } catch (e: Exception) {
            e.printStackTrace()
            uploadMessageAboveL?.onReceiveValue(null)
            uploadMessageAboveL = null
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private fun createImageUri(): Uri? {
        val status = Environment.getExternalStorageState()
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        return if (status == Environment.MEDIA_MOUNTED) {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
        } else {
            contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, ContentValues())
        }
    }
    override fun initData() {
//        val url = intent.getStringExtra("Url")
        /*url?.let {
            binding.webView.loadUrl(it)
        }*/
        binding.webView.loadUrl(Constant.URL_TRADITIONAL)
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.webView.apply {
            stopLoading()
            clearView()
            destroy()
        }
    }
    var  uploadMessageAboveL:ValueCallback<Array<Uri>>? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (uploadMessageAboveL==null)
            return
        if (resultCode!= RESULT_OK){
            uploadMessageAboveL?.onReceiveValue(null)
            uploadMessageAboveL = null
            return
        }
        if (requestCode==112){
            mImageUri?.let {
                val arr = arrayOf(it)
                uploadMessageAboveL?.onReceiveValue(arr)
                uploadMessageAboveL = null
            }
            if (mImageUri==null){
                uploadMessageAboveL?.onReceiveValue(null)
                uploadMessageAboveL = null
            }
        }else if (requestCode==101){
            val uri = data?.data
            uploadMessageAboveL = if (uri==null){
                uploadMessageAboveL?.onReceiveValue(null)
                null
            }else{
                val arr = arrayOf(uri)
                uploadMessageAboveL?.onReceiveValue(arr)
                null
            }
        }
    }

    class JavaScriptObject(private val activity: Activity) {
        @JavascriptInterface
        fun goback() {
            activity.finish()
        }

        @JavascriptInterface
        fun h5Share(json: String?) {
            Log.e("===>", json!!)
            val shareBean: ShareBean = Gson().fromJson(json, ShareBean::class.java)
            if (shareBean.url.isNullOrEmpty()) {
                return
            }
            activity as BaseVMActivity
            activity.runOnUiThread {
                activity.loadingDialog.show()
            }
            val downloadUtils = ApkDownloadUtils()
            val filePath: String =
                activity.cacheDir.toString() + File.separator + "file" + File.separator
            downloadUtils.download(
                shareBean.url,
                filePath,
                getName(shareBean.fileName),
                object : ApkDownloadUtils.OnDownloadListener {
                    override fun onDownloadSuccess(str: File?) {
                        activity.runOnUiThread {
                            activity.loadingDialog.dismiss()
                            val fileUri = FileProvider.getUriForFile(
                                activity, BuildConfig.APPLICATION_ID,
                                str!!
                            )
                            shareFile(fileUri, "text/plain")
                        }
                    }

                    override fun onDownloading(progress: Int) {
                    }

                    override fun onDownloadFailed() {
                        activity.runOnUiThread {
                            activity.loadingDialog.dismiss()
                        }
                    }

                })
        }
        fun shareFile(uri: Uri?, mimeType: String?) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = mimeType
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            activity.startActivity(Intent.createChooser(intent, "share"))
        }
        private fun getName(fileName: String?): String? {
            return if (isUrlEncoded(fileName)) {
                // URL解码
                try {
                    URLDecoder.decode(fileName, "UTF-8")
                } catch (e: UnsupportedEncodingException) {
                    throw RuntimeException(e)
                }
            } else fileName
        }
        private fun isUrlEncoded(input: String?): Boolean {
            val pattern = "%[0-9A-Fa-f]{2}"
            val regex = Pattern.compile(pattern)
            val matcher = regex.matcher(input)
            return matcher.find()
        }
    }
}
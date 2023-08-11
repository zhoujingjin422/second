package com.best.now.autoclick.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.*
import androidx.core.content.FileProvider
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.BuildConfig
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityWebPlayPianoBinding
import com.best.now.autoclick.dialog.ChooseWayPop
import com.best.now.autoclick.utils.Constant
import com.blankj.utilcode.util.ToastUtils
import java.io.File


/*** 选择服务界面 */
class WebPlayPianoActivity : BaseVMActivity() {
    private val binding by binding<ActivityWebPlayPianoBinding>(R.layout.activity_web_play_piano)
    companion object {
        fun startActivity(activity: Activity, title: String, url: String) {
            activity.startActivity(
                Intent(activity, WebPlayPianoActivity::class.java)
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
            webView.addJavascriptInterface(JavaScriptObject(this@WebPlayPianoActivity),"android")
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
        val pop = ChooseWayPop(this@WebPlayPianoActivity, { chooseFile(acceptTypes) },{takePic()}){
            uploadMessageAboveL?.onReceiveValue(null)
            uploadMessageAboveL = null
        }
        pop.showPopupWindow()
    }

    private fun fromOtherApp(){
        startActivity(Intent(this@WebPlayPianoActivity,ShowHowActivity::class.java))
        uploadMessageAboveL?.onReceiveValue(null)
        uploadMessageAboveL = null
    }
    private fun chooseFile(acceptTypes: Array<String>?) {
        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*"
        try {
            startActivityForResult(Intent.createChooser(i, "test"), 101)
        } catch (e: android.content.ActivityNotFoundException) {
            ToastUtils.showShort("File management app not found, please install file management app and try again")
        }
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
        fun goback(str:String) {
            activity.finish()
        }
    }
}
package com.best.now.autoclick.ui

import android.Manifest
import android.util.Log
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityMainBinding
import com.best.now.autoclick.databinding.ActivityShareBinding
import com.best.now.autoclick.utils.Constant
import com.best.now.autoclick.utils.ImageUtils
import com.best.now.autoclick.utils.takeScreenshotAndShare
import com.blankj.utilcode.util.ToastUtils
import com.permissionx.guolindev.PermissionX
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class ShareActivity : BaseVMActivity() {
    private val binding by binding<ActivityShareBinding>(R.layout.activity_share)
    private val ImageRes = intArrayOf(R.drawable.image_1,R.drawable.image_2,R.drawable.image_3,R.drawable.image_4)
    override fun initView() {
        binding.apply {
            val index = Random.nextInt(0,ImageRes.size)
            Log.e("index",index.toString())
            ImageUtils.loadWithBlur(this@ShareActivity,ImageRes[index],ivBack)
            ivCenter.setImageResource(ImageRes[index])
            close.setOnClickListener { finish() }
            val currentDate = Calendar.getInstance().time
            val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
            val monthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
            val dateFormat = SimpleDateFormat("d", Locale.ENGLISH)

            val dayOfWeek = dayFormat.format(currentDate)
            val month = monthFormat.format(currentDate)
            val dayOfMonth = dateFormat.format(currentDate)

            val dateString = "$dayOfWeek, $month $dayOfMonth"
            tvDay.text = dateString
            tvShare.setOnClickListener {
                //分享
                PermissionX.init(this@ShareActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            takeScreenshotAndShare(binding.cardView,true)
                        } else {
                            ToastUtils.showShort("These permissions are denied: $deniedList")
                        }
                    }
            }
            tvSave.setOnClickListener {
                //保存
                PermissionX.init(this@ShareActivity)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request { allGranted, _, deniedList ->
                        if (allGranted) {
                            takeScreenshotAndShare(binding.cardView)
                        } else {
                            ToastUtils.showShort("These permissions are denied: $deniedList")
                        }
                    }
            }
        }
    }

    override fun initData() {
    }
}
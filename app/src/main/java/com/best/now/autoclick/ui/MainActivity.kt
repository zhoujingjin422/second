package com.best.now.autoclick.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import com.android.billingclient.api.*
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityMainBinding
import com.best.now.autoclick.utils.ActionHelper
import com.best.now.autoclick.utils.Constant
import com.best.now.autoclick.utils.InPurchaseUtils
import com.best.now.autoclick.utils.adParentList
import com.best.now.autoclick.utils.isPurchased
import com.best.now.autoclick.utils.loadAd
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : BaseVMActivity() {
    companion object {
        var purchased = false
        var purchaseTime = 0L
        var productId = ""
        const val BUS_TAG_BUY_STATE_PURCHASED = "BUS_TAG_BUY_STATE_PURCHASED"
        lateinit var  inPurchaseUtils : InPurchaseUtils
    }
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        binding.apply {
            ivSetting.setOnClickListener {
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))
            }
            ivText.setOnClickListener {
                if (isPurchased(this@MainActivity)){
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                WebPlayPianoActivity.startActivity(
                                    this@MainActivity,
                                    "",
                                    Constant.URL_TRADITIONAL
                                )
                            } else {
                                ToastUtils.showShort("These permissions are denied: $deniedList")
                            }
                        }
                }
            }
            ivVoice.setOnClickListener {
                if (isPurchased(this@MainActivity)){
                    PermissionX.init(this@MainActivity)
                        .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                startActivity(Intent(this@MainActivity,RecordActivity::class.java))
                            } else {
                                ToastUtils.showShort("These permissions are denied: $deniedList")
                            }
                        }
                }
            }
        }
        inPurchaseUtils = InPurchaseUtils(this)
        inPurchaseUtils.conListener = object :InPurchaseUtils.ConnectListener{
            override fun connectSuc() {
                inPurchaseUtils.queryPurchases()
            }
        }
    }
    override fun initData() {
        loadAd(binding.advBanner)
    }
    @BusUtils.Bus(tag = BUS_TAG_BUY_STATE_PURCHASED)
    fun purchase(purchase: Purchase) {
        purchased = true
        purchaseTime = purchase.purchaseTime
        productId = GsonUtils.toJson(purchase.skus)
        ActionHelper.doAction("buy_success")

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 用户成功授予权限

            } else {
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
//                    ToastUtils.showShort("请到设置中同意存储权限")
//                } else {
//                    ToastUtils.showShort("请务必同意存储权限")
//                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        BusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }
}
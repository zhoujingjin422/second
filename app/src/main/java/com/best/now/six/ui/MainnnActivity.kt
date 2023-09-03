package com.best.now.six.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import com.android.billingclient.api.*
import com.best.now.six.BaseVMActivity
import com.best.now.six.R
import com.best.now.six.databinding.ActivityMainBinding
import com.best.now.six.utils.ActionHelper
import com.best.now.six.utils.Constant
import com.best.now.six.utils.InPurchaseUtils
import com.best.now.six.utils.isPurchased
import com.best.now.six.utils.loadAd
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.permissionx.guolindev.PermissionX

class MainnnActivity : BaseVMActivity() {
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
                startActivity(Intent(this@MainnnActivity, SettingggActivity::class.java))
            }
            tvPdf.setOnClickListener {
                if (isPurchased(this@MainnnActivity)){
                    PermissionX.init(this@MainnnActivity)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request { allGranted, _, deniedList ->
                            if (allGranted) {
                                WebPlayPianoooActivity.startActivity(
                                    this@MainnnActivity,
                                    "",
                                    Constant.URL_TRADITIONAL
                                )
                            } else {
                                ToastUtils.showShort("These permissions are denied: $deniedList")
                            }
                        }
                }


            }
            tvWeightGo.setOnClickListener {
                if (isPurchased(this@MainnnActivity)){
                    startActivity(Intent(this@MainnnActivity,WeightRecordddActivity::class.java))
                }
            }
            tvProductGo.setOnClickListener {
                if (isPurchased(this@MainnnActivity)){
                    startActivity(Intent(this@MainnnActivity,ProductttActivity::class.java))
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
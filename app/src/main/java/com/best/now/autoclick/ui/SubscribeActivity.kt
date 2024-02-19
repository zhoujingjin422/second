//package com.best.now.autoclick.ui
//
//import android.content.Intent
//import android.graphics.Paint
//import android.os.Handler
//import android.os.Looper
//import android.os.Message
////import com.android.billingclient.api.*
//import com.best.now.autoclick.BaseVMActivity
//import com.best.now.autoclick.R
//import com.best.now.autoclick.databinding.ActivitySubscribeBinding
//import com.best.now.autoclick.ui.MainActivity.Companion.BUS_TAG_BUY_STATE_PURCHASED
//import com.best.now.autoclick.utils.ActionHelper
//import com.best.now.autoclick.utils.Constant
//import com.best.now.autoclick.utils.showInterstitialAd
//import com.blankj.utilcode.util.BusUtils
//import com.blankj.utilcode.util.GsonUtils
//import com.blankj.utilcode.util.LogUtils
//import com.blankj.utilcode.util.ToastUtils
//
///**
//author:zhoujingjin
//date:2022/11/19
// */
//class SubscribeActivity:BaseVMActivity() {
//    private val binding by binding<ActivitySubscribeBinding>(R.layout.activity_subscribe)
//
//    override fun initView() {
//        binding.apply {
//            tvSubTitle.setOnClickListener {
//                MainActivity.inPurchaseUtils.buy(Constant.VIP_MONTH)
//            }
//            next.setOnClickListener {
//                MainActivity.inPurchaseUtils.buy(Constant.VIP_MONTH)
//            }
//            ivClose.setOnClickListener {
//                finish()
//            }
//        }
//    }
//
//    override fun initData() {
////        ActionHelper.doAction("sellpage")
//    }
//    @BusUtils.Bus(tag = BUS_TAG_BUY_STATE_PURCHASED)
//    fun purchase(purchase: Purchase) {
//        runOnUiThread {
//            finish()
//        }
//    }
//    override fun onStart() {
//        super.onStart()
//        BusUtils.register(this)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        BusUtils.unregister(this)
//    }
//}
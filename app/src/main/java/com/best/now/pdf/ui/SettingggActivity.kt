package com.best.now.pdf.ui

import android.content.Intent
import android.view.View
import com.android.billingclient.api.Purchase
import com.best.now.pdf.BaseVMActivity
import com.best.now.pdf.R
import com.best.now.pdf.databinding.ActivitySettingBinding
import com.best.now.pdf.utils.Constant
import com.best.now.pdf.utils.loadAd
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.TimeUtils

/**
author:zhoujingjin
date:2022/11/20
 */
class SettingggActivity:BaseVMActivity() {
    private val binding by binding<ActivitySettingBinding>(R.layout.activity_setting)
    override fun initView() {
        binding.apply {
            setUiState()
            flPolicy.setOnClickListener {
                WebbbActivity.startActivity(
                    this@SettingggActivity,
                    "Privacy Policy",
                    Constant.URL_PRIVACY_POLICY
                )
            }
//            flService.setOnClickListener {
//                WebActivity.startActivity(
//                    this@SettingActivity,
//                    "Terms of Service",
//                    Constant.URL_TERMS_OF_USE
//                )
//            }
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
        }
    }

    private fun setUiState() {
        if (!MainnnActivity.purchased){
//            binding.ivVip.visibility = View.VISIBLE
            binding.btnGetVip.visibility = View.VISIBLE
            binding.llText.visibility = View.GONE
            binding.btnGetVip.setOnClickListener {
                startActivity(Intent(this@SettingggActivity,SubscribeeeActivity::class.java))
            }
        }else{
            binding.llText.visibility = View.VISIBLE
//            binding.ivVip.visibility = View.GONE
            binding.btnGetVip.visibility = View.GONE
            var time = 30 * 24 * 3600 * 1000L
            binding.tvDate.text = "Membership valid until：${TimeUtils.millis2String(
                MainnnActivity.purchaseTime + time,
                "yyyy.MM.dd"
            )}"

        }

    }
    private fun setUiStateSelf(purchaseTime: Long) {
        binding.llText.visibility = View.VISIBLE
//        binding.ivVip.visibility = View.GONE
        binding.btnGetVip.visibility = View.GONE
        var time = 30 * 24 * 3600 * 1000L
        binding.tvDate.text = "Membership valid until：${TimeUtils.millis2String(
            purchaseTime+ time,
            "yyyy.MM.dd"
        )}"
    }
    @BusUtils.Bus(tag = MainnnActivity.BUS_TAG_BUY_STATE_PURCHASED)
    fun purchase(purchase: Purchase) {
        runOnUiThread {
            setUiStateSelf(purchase.purchaseTime)
        }
    }
    override fun initData() {
        loadAd(binding.advBanner)
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
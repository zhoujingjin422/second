package com.best.now.autoclick.ui

import android.content.Intent
import android.view.View
import com.android.billingclient.api.Purchase
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivitySettingBinding
import com.best.now.autoclick.utils.Constant
import com.best.now.autoclick.utils.loadAd
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.TimeUtils

/**
author:zhoujingjin
date:2022/11/20
 */
class SettingActivity:BaseVMActivity() {
    private val binding by binding<ActivitySettingBinding>(R.layout.activity_setting)
    override fun initView() {
        binding.apply {
            setUiState()
            flPolicy.setOnClickListener {
                WebActivity.startActivity(
                    this@SettingActivity,
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
        if (!MainActivity.purchased){
            binding.ivVip.visibility = View.VISIBLE
            binding.btnGetVip.visibility = View.VISIBLE
            binding.llText.visibility = View.GONE
            binding.btnGetVip.setOnClickListener {
                startActivity(Intent(this@SettingActivity,SubscribeActivity::class.java))
            }
        }else{
            binding.llText.visibility = View.VISIBLE
            binding.ivVip.visibility = View.GONE
            binding.btnGetVip.visibility = View.GONE
            var time = 30 * 24 * 3600 * 1000L
            binding.tvDate.text = "Membership valid until：${TimeUtils.millis2String(
                MainActivity.purchaseTime + time,
                "yyyy.MM.dd"
            )}"

        }

    }
    private fun setUiStateSelf(purchaseTime: Long) {
        binding.llText.visibility = View.VISIBLE
        binding.ivVip.visibility = View.GONE
        binding.btnGetVip.visibility = View.GONE
        var time = 30 * 24 * 3600 * 1000L
        binding.tvDate.text = "Membership valid until：${TimeUtils.millis2String(
            purchaseTime+ time,
            "yyyy.MM.dd"
        )}"
    }
    @BusUtils.Bus(tag = MainActivity.BUS_TAG_BUY_STATE_PURCHASED)
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
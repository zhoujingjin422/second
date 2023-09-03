package com.best.now.six.ui

import com.android.billingclient.api.*
import com.best.now.six.BaseVMActivity
import com.best.now.six.R
import com.best.now.six.databinding.ActivitySubscribeBinding
import com.best.now.six.ui.MainnnActivity.Companion.BUS_TAG_BUY_STATE_PURCHASED
import com.best.now.six.utils.ActionHelper
import com.best.now.six.utils.Constant
import com.blankj.utilcode.util.BusUtils

/**
author:zhoujingjin
date:2022/11/19
 */
class SubscribeeeActivity:BaseVMActivity() {
    private val binding by binding<ActivitySubscribeBinding>(R.layout.activity_subscribe)

    override fun initView() {
        binding.apply {
            tvSubTitle.setOnClickListener {
                MainnnActivity.inPurchaseUtils.buy(Constant.VIP_MONTH)
            }
            next.setOnClickListener {
                MainnnActivity.inPurchaseUtils.buy(Constant.VIP_MONTH)
            }
            ivClose.setOnClickListener {
                finish()
            }
        }
    }

    override fun initData() {
        ActionHelper.doAction("sellpage")
    }
    @BusUtils.Bus(tag = BUS_TAG_BUY_STATE_PURCHASED)
    fun purchase(purchase: Purchase) {
        runOnUiThread {
            finish()
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
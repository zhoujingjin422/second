package com.best.now.seven.ui

import com.best.now.seven.BaseVMActivity
import com.best.now.seven.R
import com.best.now.seven.databinding.ActivityBmiBinding
import com.best.now.seven.dialog.BmiPop


/**
author:zhoujingjin
date:2023/9/3
 */
class BmiActivity:BaseVMActivity() {
    private val binding by binding<ActivityBmiBinding>(R.layout.activity_bmi)
    override fun initView() {
        binding.apply{
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvCalculate.setOnClickListener {
                val bmiPop = BmiPop(this@BmiActivity,"")
                bmiPop.showPopupWindow()
            }

        }
    }
    override fun initData() {

    }
}
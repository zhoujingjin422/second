package com.best.now.autoclick.ui

import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityFlashLightBinding
import com.best.now.autoclick.databinding.ActivityWhiteBinding

class WhiteActivity:BaseVMActivity() {
    private val binding by binding<ActivityWhiteBinding>(R.layout.activity_white)
    private var backPressedTime = 0L
    override fun initView() {
        binding.content.setOnClickListener {
            if (backPressedTime + 3000 > System.currentTimeMillis()) {
                finish()
            }
            backPressedTime = System.currentTimeMillis()
        }
    }

    override fun initData() {
    }
}
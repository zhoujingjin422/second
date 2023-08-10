package com.best.now.autoclick.ui

import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityShowHowBinding
import com.best.now.autoclick.databinding.ActivityWebPlayPianoBinding

/**
author:zhoujingjin
date:2023/8/10
 */
class ShowHowActivity:BaseVMActivity() {
    private val binding by binding<ActivityShowHowBinding>(R.layout.activity_show_how)
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
        }
    }

    override fun initData() {
    }
}
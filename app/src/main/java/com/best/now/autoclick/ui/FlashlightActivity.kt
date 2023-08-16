package com.best.now.autoclick.ui

import android.content.Intent
import android.os.Build
import android.view.View
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityFlashLightBinding
import com.best.now.autoclick.utils.CameraAndFlashProvider
import com.best.now.autoclick.viewmodel.FlashLightViewModel

/**
author:zhoujingjin
date:2023/8/15
 */
class FlashlightActivity:BaseVMActivity() {
    private val binding by binding<ActivityFlashLightBinding>(R.layout.activity_flash_light)
    private lateinit var viewModel: FlashLightViewModel
    private val cameraAndFlashProvider = CameraAndFlashProvider(this)
    override fun initView() {
        viewModel = FlashLightViewModel()
        binding.apply {
            toolBar.title = "flashlight"
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
            ivSdt.setOnClickListener {
                viewModel.lightOn.postValue(!viewModel.lightOn.value!!)
            }
            ivSd.setOnClickListener {
                viewModel.ssOn.postValue(!viewModel.ssOn.value!!)
            }
            ivWhite.setOnClickListener {
                startActivity(Intent(this@FlashlightActivity,WhiteActivity::class.java))
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                llTop.visibility = View.VISIBLE
            }else{
                llTop.visibility = View.GONE
            }
        }
    }

    override fun initData() {
        viewModel.lightOn.observe(this){
            if (it){
                binding.ivLight.setImageResource(R.mipmap.icon_light)
                binding.ivSdt.setImageResource(R.mipmap.icon_sdt_on)
                cameraAndFlashProvider.turnFlashlightOn()
            }else{
                binding.ivLight.setImageResource(R.mipmap.icon_dark)
                cameraAndFlashProvider.turnFlashlightOff()
            }
        }
        viewModel.ssOn.observe(this){
            if (it){
                binding.ivSd.setImageResource(R.mipmap.icon_sd_on)
                if (viewModel.lightOn.value==false){
                    viewModel.lightOn.postValue(true)
                }
                viewModel.startTimer(cameraAndFlashProvider)
            }else{
                binding.ivSd.setImageResource(R.mipmap.icon_sd_off)
                viewModel.release()
                viewModel.lightOn.postValue(true)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraAndFlashProvider.turnFlashlightOff()
        viewModel.release()
    }
}
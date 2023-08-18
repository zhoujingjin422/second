package com.best.now.autoclick.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.view.marginEnd
import androidx.core.view.marginRight
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityFlashLightBinding
import com.best.now.autoclick.ext.dp2px
import com.best.now.autoclick.utils.CameraAndFlashProvider
import com.best.now.autoclick.viewmodel.FlashLightViewModel

/**
author:zhoujingjin
date:2023/8/15
 */
class FlashlightActivity:BaseVMActivity() {
    private val binding by binding<ActivityFlashLightBinding>(R.layout.activity_flash_light)
    private lateinit var viewModel: FlashLightViewModel
    private lateinit var cameraAndFlashProvider :CameraAndFlashProvider
    override fun initView() {
        viewModel = FlashLightViewModel()
        cameraAndFlashProvider =CameraAndFlashProvider.getInstance(this)
        cameraAndFlashProvider.setonTorchModeChanged {
            if (viewModel.type.value==1){
                viewModel.lightOn.postValue(it)
            }
        }
        binding.apply {
            toolBar.title = "flashlight"
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener { finish() }
            ivSdt.setOnClickListener {
                if (viewModel.type.value==1)
                    return@setOnClickListener
                viewModel.type.postValue(1)
                if (viewModel.lightOn.value==true){
                    viewModel.release()
                    cameraAndFlashProvider.turnFlashlightOn()
                }
            }
            ivSd.setOnClickListener {
                if (viewModel.type.value==2)
                    return@setOnClickListener
                viewModel.type.postValue(2)
                if (viewModel.lightOn.value==true){
                    viewModel.release()
                    viewModel.startTimer(cameraAndFlashProvider)
                }
            }
            ivWhite.setOnClickListener {
                startActivity(Intent(this@FlashlightActivity,WhiteActivity::class.java))
            }
            ivLight.setOnClickListener {
                viewModel.lightOn.postValue(!viewModel.lightOn.value!!)
            }
            flMus.setOnClickListener {
                if (viewModel.num==1){
                    return@setOnClickListener
                }
                viewModel.num--
                if (viewModel.num<1)
                    viewModel.num=1
                showBottomView()
                if (viewModel.lightOn.value==true){
                    viewModel.release()
                    viewModel.startTimer(cameraAndFlashProvider)
                }
            }
            flAdd.setOnClickListener {
                if (viewModel.num==5){
                    return@setOnClickListener
                }
                viewModel.num++
                if (viewModel.num>5)
                    viewModel.num=5
                showBottomView()
                if (viewModel.lightOn.value==true){
                    viewModel.release()
                    viewModel.startTimer(cameraAndFlashProvider)
                }
            }
        }
    }

    override fun initData() {
        viewModel.lightOn.observe(this){
            if (it){
                binding.ivLight.setImageResource(R.mipmap.icon_light)
                if (viewModel.type.value==1){
                    cameraAndFlashProvider.turnFlashlightOn()
                }else{
                    viewModel.release()
                    viewModel.startTimer(cameraAndFlashProvider)
                }
            }else{
                binding.ivLight.setImageResource(R.mipmap.icon_dark)
                cameraAndFlashProvider.turnFlashlightOff()
                if (viewModel.type.value==1){
                    cameraAndFlashProvider.turnFlashlightOff()
                }else{
                    viewModel.release()
                    cameraAndFlashProvider.turnFlashlightOff()
                }
            }
        }
        viewModel.type.observe(this){
            if (it==1){
                binding.ivSdt.setImageResource(R.mipmap.icon_sdt_on)
                binding.ivSd.setImageResource(R.mipmap.icon_sd_off)
                binding.flBottom.visibility = View.GONE
            }else{
                binding.ivSd.setImageResource(R.mipmap.icon_sd_on)
                binding.ivSdt.setImageResource(R.mipmap.icon_sdt_off)
                showBottomView()
                binding.flBottom.visibility = View.VISIBLE
            }
        }
    }

    private fun showBottomView() {
       binding.view1.visibility = View.VISIBLE
       binding.view2.visibility = if(viewModel.num>=2) View.VISIBLE else View.GONE
       binding.view3.visibility = if(viewModel.num>=3) View.VISIBLE else View.GONE
       binding.view4.visibility = if(viewModel.num>=4) View.VISIBLE else View.GONE
       binding.view5.visibility = if(viewModel.num>=5) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraAndFlashProvider.turnFlashlightOff()
        cameraAndFlashProvider.stopBackgroundThread()
        viewModel.release()
    }
}
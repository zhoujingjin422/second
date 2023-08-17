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
            if (viewModel.ssOn.value==false){
                viewModel.lightOn.postValue(it)
            }
        }
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
            ivLight.setOnClickListener {
                viewModel.lightOn.postValue(!viewModel.lightOn.value!!)
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                llTop.visibility = View.VISIBLE
//                seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
//                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                        cameraAndFlashProvider.changeStrengthLevel(p1)
//                    }
//
//                    override fun onStartTrackingTouch(p0: SeekBar?) {
//                    }
//
//                    override fun onStopTrackingTouch(p0: SeekBar?) {
//
//                    }
//                })
//            }else{
//                llTop.visibility = View.GONE
//            }

            flMus.setOnClickListener {
                if (viewModel.num==1){
                    return@setOnClickListener
                }
                viewModel.num--
                if (viewModel.num<1)
                    viewModel.num=1
                showBottomView()
                viewModel.release()
                viewModel.startTimer(cameraAndFlashProvider)
            }
            flAdd.setOnClickListener {
                if (viewModel.num==5){
                    return@setOnClickListener
                }
                viewModel.num++
                if (viewModel.num>5)
                    viewModel.num=5
                showBottomView()
                viewModel.release()
                viewModel.startTimer(cameraAndFlashProvider)
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
                binding.ivSdt.setImageResource(R.mipmap.icon_sdt_off)
                cameraAndFlashProvider.turnFlashlightOff()
                viewModel.ssOn.postValue(false)
            }
        }
        viewModel.ssOn.observe(this){
            if (it){
                binding.ivSd.setImageResource(R.mipmap.icon_sd_on)
                if (viewModel.lightOn.value==false){
                    viewModel.lightOn.postValue(true)
                }
                viewModel.startTimer(cameraAndFlashProvider)
                binding.flBottom.visibility = View.VISIBLE
                showBottomView()
            }else{
                binding.ivSd.setImageResource(R.mipmap.icon_sd_off)
                viewModel.release()
                if (viewModel.lightOn.value==true){
                    viewModel.lightOn.postValue(true)
                }
                binding.flBottom.visibility = View.GONE
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
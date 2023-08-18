package com.best.now.autoclick.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.now.autoclick.utils.CameraAndFlashProvider
import java.util.Timer
import java.util.TimerTask

class FlashLightViewModel:ViewModel() {
    var type = MutableLiveData(1)
    var lightOn = MutableLiveData(false)
    private  var timer: Timer?=null
    private var onOff = false
     var num = 1
    fun startTimer(cameraAndFlashProvider: CameraAndFlashProvider) {
        timer= Timer()
        val timerTask=object : TimerTask(){
            override fun run() {
                if (type.value==2){
                    onOff = !onOff
                    if (onOff){
                        cameraAndFlashProvider.turnFlashlightOn()
                    }else{
                        cameraAndFlashProvider.turnFlashlightOff()
                    }
                }
            }
        }
        timer?.schedule(timerTask,0,500/num.toLong())
    }
    fun release(){
        timer?.cancel()
    }
}
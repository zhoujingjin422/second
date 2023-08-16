package com.best.now.autoclick.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.now.autoclick.utils.CameraAndFlashProvider
import java.util.Timer
import java.util.TimerTask

class FlashLightViewModel:ViewModel() {
    var lightOn = MutableLiveData(true)
    var ssOn = MutableLiveData(false)
    private  var timer: Timer?=null
    private var onOff = false
    fun startTimer(cameraAndFlashProvider: CameraAndFlashProvider) {
        timer= Timer()
        val timerTask=object : TimerTask(){
            override fun run() {
                if (ssOn.value==true){
                    onOff = !onOff
                    if (onOff){
                        cameraAndFlashProvider.turnFlashlightOn()
                    }else{
                        cameraAndFlashProvider.turnFlashlightOff()
                    }
                }
            }
        }
        timer?.schedule(timerTask,0,100)
    }
    fun release(){
        timer?.cancel()
    }
}
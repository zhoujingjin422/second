package com.best.now.six.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Timer
import java.util.TimerTask

/**
 * 录音的viewModel
 */
class RecordViewModel:ViewModel() {
    /**
     * 录音的状态，来改变按钮的显示与否
     * 0 未开始录音  1正在录音  2录音停止  3 播放录音
     */
    var recordState = MutableLiveData(0)
    private  var timer: Timer?=null
    private var currentSecond=0

    private  var second:MutableLiveData<Int>  = MutableLiveData()
    fun getCurrentSecond():MutableLiveData<Int>{
        return second
    }

    fun startTimer(){
        pause= false
        timer=Timer()
        currentSecond=0
        val timerTask=object :TimerTask(){
            override fun run() {
                if (!pause){
                    currentSecond++
                    second.postValue(currentSecond)
                }
            }
        }
        timer?.schedule(timerTask,1000,1000)
    }
    var pause = false
    fun pauseTimer(){
        pause = true
    }
    fun continueTimer(){
        pause = false
    }
    fun restartTimer(){
        timer?.cancel()
        currentSecond = 0
        startTimer()
    }
    fun release(){
        second.postValue(0)
        timer?.cancel()
    }
}
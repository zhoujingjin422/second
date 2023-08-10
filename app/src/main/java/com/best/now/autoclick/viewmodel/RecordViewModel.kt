package com.best.now.autoclick.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 录音的viewModel
 */
class RecordViewModel:ViewModel() {
    /**
     * 录音的状态，来改变按钮的显示与否
     * 0 未开始录音  1正在录音  2录音停止  3 播放录音
     */
    var recordState = MutableLiveData<Int>(0)
}
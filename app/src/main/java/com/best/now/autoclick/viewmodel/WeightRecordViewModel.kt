package com.best.now.autoclick.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.now.autoclick.bean.WeightBean

/**
author:zhoujingjin
date:2023/8/13
 */
class WeightRecordViewModel:ViewModel() {
    var isEdit = MutableLiveData(false)
    var data = MutableLiveData(mutableListOf<WeightBean>())
}
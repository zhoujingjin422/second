package com.best.now.six.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.best.now.six.bean.ProductBean

/**
author:zhoujingjin
date:2023/8/13
 */
class ProductViewModel:ViewModel() {
    var productBean = MutableLiveData<ProductBean?>()
}
package com.best.now.seven.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.seven.R
import com.best.now.seven.databinding.PopBmiBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class BmiPop(context:Context,val bmi:String):BasePopupWindow(context) {

    init {
        setContentView(R.layout.pop_bmi)
        val bindingUtil = DataBindingUtil.bind<PopBmiBinding>(contentView)
        bindingUtil?.apply {
        tvResult.text = bmi
            ivClose.setOnClickListener {
                dismiss()
            }
        }
        popupGravity = Gravity.CENTER
    }

}
package com.best.now.autoclick.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.PopChooseWayBinding
import com.best.now.autoclick.databinding.PopSuccessfullyBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class SuccessfullyPop(context:Context, type:Int = 0):BasePopupWindow(context) {

    init {
        setContentView(R.layout.pop_successfully)
       val bind =  DataBindingUtil.bind<PopSuccessfullyBinding>(contentView)
        bind?.apply {
            if (type==1){
                iv.setImageResource(R.drawable.icon_wrong)
                tvContent.text = "Please enter the complete to-do event content"
            }
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }

    override fun showPopupWindow() {
        super.showPopupWindow()
        contentView?.postDelayed({
             dismiss()
        },3000L)
    }
}
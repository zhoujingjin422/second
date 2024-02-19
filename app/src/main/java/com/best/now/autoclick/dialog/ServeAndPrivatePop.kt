package com.best.now.autoclick.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.PopChooseWayBinding
import com.best.now.autoclick.databinding.PopServePrivateBinding
import com.best.now.autoclick.databinding.PopSuccessfullyBinding
import com.best.now.autoclick.ext.putSpValue
import razerdp.basepopup.BasePopupWindow
import kotlin.system.exitProcess

/**
author:zhoujingjin
date:2023/8/10
 */
class ServeAndPrivatePop(context:Context):BasePopupWindow(context) {

    init {
        setContentView(R.layout.pop_serve_private)
       val bind =  DataBindingUtil.bind<PopServePrivateBinding>(contentView)
        bind?.apply {
            tvAgree.setOnClickListener {
                context.putSpValue("hasShowPrivacy",true)
                dismiss()
            }
            tvNot.setOnClickListener {
                dismiss()
                exitProcess(0)
            }
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }
}
package com.best.now.autoclick.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.PopChooseWayBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class ChooseWayPop(context:Context, action: () -> Unit, action2:()-> Unit, action3:()-> Unit):BasePopupWindow(context) {

//    override fun onCreateAnimateView(): View {
//        return LayoutInflater.from(context).inflate(R.layout.pop_choose_way,null)
//    }
//    override fun createPopupById(layoutId: Int): View {
//        return super.createPopupById(layoutId)
//    }
    init {
        setContentView(R.layout.pop_choose_way)
       val bind =  DataBindingUtil.bind<PopChooseWayBinding>(contentView)
        bind?.apply {
            ivApp.setOnClickListener {
                action2.invoke()
                dismiss()
            }
            ivFile.setOnClickListener {
                action.invoke()
                dismiss()
            }
            ivCancel.setOnClickListener {
                action3.invoke()
                dismiss()
            }
        }
    setOutSideDismiss(false)
        popupGravity = Gravity.BOTTOM
    }
}
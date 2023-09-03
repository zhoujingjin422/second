package com.best.now.six.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.six.R
import com.best.now.six.databinding.PopTypeWayBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class ChooseTypePop(context:Context, action: (type:Int) -> Unit):BasePopupWindow(context) {

//    override fun onCreateAnimateView(): View {
//        return LayoutInflater.from(context).inflate(R.layout.pop_choose_way,null)
//    }
//    override fun createPopupById(layoutId: Int): View {
//        return super.createPopupById(layoutId)
//    }
    init {
        setContentView(R.layout.pop_type_way)
       val bind =  DataBindingUtil.bind<PopTypeWayBinding>(contentView)
        bind?.apply {
            tvDay.setOnClickListener {
                action.invoke(1)
                dismiss()
            }
            tvMonth.setOnClickListener {
                action.invoke(2)
                dismiss()
            }
            tvYear.setOnClickListener {
                action.invoke(3)
                dismiss()
            }
            ivCancel.setOnClickListener {
                dismiss()
            }
        }
        setOutSideDismiss(false)
        popupGravity = Gravity.BOTTOM
    }
}
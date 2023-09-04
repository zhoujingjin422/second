package com.best.now.six.dialog

import android.content.Context
import android.view.Gravity
import androidx.databinding.DataBindingUtil
import com.best.now.six.R
import com.best.now.six.databinding.PopChooseWayBinding
import razerdp.basepopup.BasePopupWindow

/**
author:zhoujingjin
date:2023/8/10
 */
class SavePop(context:Context):BasePopupWindow(context) {

//    override fun onCreateAnimateView(): View {
//        return LayoutInflater.from(context).inflate(R.layout.pop_choose_way,null)
//    }
//    override fun createPopupById(layoutId: Int): View {
//        return super.createPopupById(layoutId)
//    }
    init {
        setContentView(R.layout.toast_view)

    setOutSideDismiss(false)
        popupGravity = Gravity.CENTER
    }

    override fun showPopupWindow() {
        super.showPopupWindow()
        contentView.postDelayed({
            dismiss()
        },2000L)
    }
}
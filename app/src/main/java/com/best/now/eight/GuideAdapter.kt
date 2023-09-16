package com.best.now.eight
import com.best.now.eight.databinding.GuideItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2022/11/19
 */
class GuideAdapter(val nextClick:NextClickCallBack):BaseQuickAdapter<Int, BaseDataBindingHolder<GuideItemBinding>>(R.layout.guide_item) {
    override fun convert(holder: BaseDataBindingHolder<GuideItemBinding>, item: Int) {
        val position = getItemPosition(item)
        holder.dataBinding?.apply {
            ivImage.setImageResource(item)
            ivImage.setOnClickListener {
                nextClick.clickNext(position)
            }
        }
    }
}
interface NextClickCallBack{
    fun clickNext(position:Int)
}

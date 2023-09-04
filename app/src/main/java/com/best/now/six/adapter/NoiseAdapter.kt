package com.best.now.six.adapter

import android.view.View
import com.best.now.six.R
import com.best.now.six.bean.NoiseBean
import com.best.now.six.databinding.ItemNoiseBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2023/8/13
 */
class NoiseAdapter( var isPlaying:Boolean = false,private val action:(NoiseBean)->Unit):BaseQuickAdapter<NoiseBean, BaseDataBindingHolder<ItemNoiseBinding>>(R.layout.item_noise) {
   private var nowItem:NoiseBean? = null
    override fun convert(holder: BaseDataBindingHolder<ItemNoiseBinding>, item: NoiseBean) {
        holder.dataBinding?.apply {
            tvName.text = item.name
            iv.setImageResource(item.resId)
            iv.setOnClickListener {
                action.invoke(item)
                nowItem = item
            }
            if (nowItem==item){
                ivTop.visibility = View.VISIBLE
            }else{
                ivTop.visibility = View.GONE
            }
            if (isPlaying){
               ivTop.setImageResource(R.drawable.iv_pause)
            }else{
                ivTop.setImageResource(R.drawable.iv_play)
            }
        }
    }
}
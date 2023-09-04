package com.best.now.six.adapter

import com.best.now.six.R
import com.best.now.six.bean.NoiseBean
import com.best.now.six.databinding.ItemNoiseBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2023/8/13
 */
class NoiseAdapter(private val action:(NoiseBean)->Unit):BaseQuickAdapter<NoiseBean, BaseDataBindingHolder<ItemNoiseBinding>>(R.layout.item_noise) {
    override fun convert(holder: BaseDataBindingHolder<ItemNoiseBinding>, item: NoiseBean) {
        holder.dataBinding?.apply {
            tvName.text = item.name
            iv.setImageResource(item.resId)
            iv.setOnClickListener {
                action.invoke(item)
            }
        }
    }
}
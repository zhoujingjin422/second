package com.best.now.pdf.adapter

import com.best.now.pdf.R
import com.best.now.pdf.bean.WeightBean
import com.best.now.pdf.databinding.ItemWeightBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2023/8/13
 */
class WeightAdapter(private val action: (data:MutableList<WeightBean>) -> Unit):BaseQuickAdapter<WeightBean, BaseDataBindingHolder<ItemWeightBinding>>(R.layout.item_weight) {
    override fun convert(holder: BaseDataBindingHolder<ItemWeightBinding>, item: WeightBean) {
        holder.dataBinding?.apply {
            tvWeight.text = item.weight
            tvDate.text = item.date
            tvTime.text = item.time
            ivDelete.setOnClickListener {
                data.remove(item)
                action.invoke(data)
                notifyDataSetChanged()
            }
        }
    }
}
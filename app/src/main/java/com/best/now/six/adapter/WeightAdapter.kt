package com.best.now.six.adapter

import com.best.now.six.R
import com.best.now.six.bean.WeightBean
import com.best.now.six.databinding.ItemWeightBinding
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
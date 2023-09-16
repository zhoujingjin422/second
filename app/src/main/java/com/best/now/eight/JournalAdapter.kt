package com.best.now.eight

import android.view.View
import com.best.now.eight.bean.JournalBean
import com.best.now.eight.databinding.JournalItemBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2023/9/16
 */
class JournalAdapter: BaseQuickAdapter<JournalBean, BaseDataBindingHolder<JournalItemBinding>>(R.layout.journal_item) {
    override fun convert(holder: BaseDataBindingHolder<JournalItemBinding>, item: JournalBean) {
        holder.dataBinding?.apply {
            tvDay.text = item.day
            tvContent.text = item.title
            val position = getItemPosition(item)
            if (position==0||item.year!=data[position-1].year){
                tvYear.visibility = View.VISIBLE
                tvYear.text = item.year.toString()
            }else{
                tvYear.visibility = View.GONE
            }
        }
    }
}
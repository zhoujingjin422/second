package com.best.now.autoclick.adapter

import android.graphics.Paint
import com.best.now.autoclick.R
import com.best.now.autoclick.bean.ContentBean
import com.best.now.autoclick.databinding.TodoItem2Binding
import com.best.now.autoclick.databinding.TodoItemBinding
import com.best.now.autoclick.ext.putSpValue
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

/**
author:zhoujingjin
date:2022/11/19
 */
class TodoAdapter2(val type:Int):BaseQuickAdapter<ContentBean, BaseDataBindingHolder<TodoItem2Binding>>(R.layout.todo_item_2) {
    override fun convert(holder: BaseDataBindingHolder<TodoItem2Binding>, item: ContentBean) {
        val position = getItemPosition(item)
        holder.dataBinding?.apply {
            cb.text = item.conent
            cb.isChecked = item.done
            if (item.done){
                cb.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                cb.isClickable = false
            }else{
                cb.paint.flags = 0
            }
            cb.setOnCheckedChangeListener { _, b ->
                item.done = b
                if (item.done){
                    cb.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
                    cb.isClickable = false
                }else{
                    cb.paint.flags = 0
                }
                data[position] = item
               context. putSpValue("content${type}", GsonUtils.getGson().toJson(data))
            }
        }
    }
}

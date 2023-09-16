package com.best.now.eight.ui

import androidx.core.widget.addTextChangedListener
import com.best.now.eight.BaseVMActivity
import com.best.now.eight.R
import com.best.now.eight.bean.JournalBean
import com.best.now.eight.databinding.ActivityJournalBinding
import com.best.now.eight.ext.getSpValue
import com.best.now.eight.ext.putSpValue
import com.blankj.utilcode.util.BusUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

/**
author:zhoujingjin
date:2023/9/16
 */
class JournalActivity:BaseVMActivity() {
    private val binding by binding<ActivityJournalBinding>(R.layout.activity_journal)
    private lateinit var data:JournalBean
    private lateinit var list:MutableList<JournalBean>
    override fun initView() {
        val index = intent.getIntExtra("index",-1)
        val type = object : TypeToken<List<JournalBean>>() {}.type
        val listString = getSpValue("dataList","")
        list = if (listString.isNullOrEmpty()){
            mutableListOf<JournalBean>()
        }else{
            Gson().fromJson<MutableList<JournalBean>>(listString,type)
        }
        data = if (index==-1){
            val calendar = Calendar.getInstance()
            val month = calendar.get(Calendar.MONTH)+1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            JournalBean("","",calendar.get(Calendar.YEAR),"${month}.${day}")
        }else{
            list[index]
        }
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvSave.setOnClickListener {
                //保存
                if (list.isEmpty()){
                    list.add(data)
                }else{
                    if (index==-1){
                        list.add(0,data)
                    }else{
                        list[index] = data
                    }
                }
                putSpValue("dataList",Gson().toJson(list))
                BusUtils.post(MainnnActivity.BUS_TAG_DATA_CHANGE, true)
                finish()
            }
            etTitle.setText(data.title)
            etContent.setText(data.content)
            etTitle.addTextChangedListener {
                data.title = it.toString()
            }
            etContent.addTextChangedListener {
                data.content = it.toString()
            }
        }
    }

    override fun initData() {

    }
}
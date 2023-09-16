package com.best.now.eight.ui

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.now.eight.BaseVMActivity
import com.best.now.eight.JournalAdapter
import com.best.now.eight.R
import com.best.now.eight.bean.JournalBean
import com.best.now.eight.databinding.ActivityJournalListBinding
import com.best.now.eight.ext.getSpValue
import com.blankj.utilcode.util.BusUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
author:zhoujingjin
date:2023/9/16
 */
class JournalListActivity:BaseVMActivity() {
    private val binding by binding<ActivityJournalListBinding>(R.layout.activity_journal_list)
    private lateinit var adapter: JournalAdapter
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvPassword.setOnClickListener {
                startActivity(Intent(this@JournalListActivity,PassWordActivity::class.java).putExtra("change",true))
            }
            add.setOnClickListener {
                startActivity(Intent(this@JournalListActivity,JournalActivity::class.java))
            }
            recycler.layoutManager = LinearLayoutManager(this@JournalListActivity)
            adapter= JournalAdapter()
            recycler.adapter =adapter
        }
        adapter.setOnItemClickListener { _, _, position ->
            startActivity(Intent(this,JournalActivity::class.java).putExtra("index",position))
        }
    }

    override fun initData() {
        val dataString = getSpValue("dataList","")
        if (dataString.isNotEmpty()){
            val type = object : TypeToken<List<JournalBean>>() {}.type
            val list = Gson().fromJson<MutableList<JournalBean>>(dataString,type)
            adapter.setNewInstance(list)
        }
    }
    @BusUtils.Bus(tag = MainnnActivity.BUS_TAG_DATA_CHANGE)
    fun purchase(change: Boolean) {
       initData()
    }
    override fun onStart() {
        super.onStart()
        BusUtils.register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        BusUtils.unregister(this)
    }
}
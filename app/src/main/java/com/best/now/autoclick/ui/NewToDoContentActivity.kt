package com.best.now.autoclick.ui

import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.bean.ContentBean
import com.best.now.autoclick.databinding.ActivityNewtodoContentBinding
import com.best.now.autoclick.databinding.ActivityQuadrantListBinding
import com.best.now.autoclick.ext.getSpValue
import com.best.now.autoclick.ext.putSpValue
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.reflect.TypeToken

class NewToDoContentActivity: BaseVMActivity() {
    private val binding by binding<ActivityNewtodoContentBinding>(R.layout.activity_newtodo_content)
    var type = 1
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tv1.setOnClickListener {
                tv1.setBackgroundResource(R.drawable.shape_blue_selected)
                tv2.setBackgroundResource(R.drawable.shape_orange)
                tv3.setBackgroundResource(R.drawable.shape_blue_1)
                tv4.setBackgroundResource(R.drawable.shape_orange_1)
                type = 1
            }
            tv2.setOnClickListener {
                tv1.setBackgroundResource(R.drawable.shape_blue)
                tv2.setBackgroundResource(R.drawable.shape_orange_selected)
                tv3.setBackgroundResource(R.drawable.shape_blue_1)
                tv4.setBackgroundResource(R.drawable.shape_orange_1)
                type = 2
            }
            tv3.setOnClickListener {
                tv1.setBackgroundResource(R.drawable.shape_blue)
                tv2.setBackgroundResource(R.drawable.shape_orange)
                tv3.setBackgroundResource(R.drawable.shape_blue_selected_1)
                tv4.setBackgroundResource(R.drawable.shape_orange_1)
                type = 3
            }
            tv4.setOnClickListener {
                tv1.setBackgroundResource(R.drawable.shape_blue)
                tv2.setBackgroundResource(R.drawable.shape_orange)
                tv3.setBackgroundResource(R.drawable.shape_blue_1)
                tv4.setBackgroundResource(R.drawable.shape_orange_selected_1)
                type = 4
            }
            ivSave.setOnClickListener {
                if (etStart.text.toString().trim().isEmpty()){
                    ToastUtils.showShort("Please enter the complete to-do event content")
                    return@setOnClickListener
                }

                val contentListStr = getSpValue("content${type}","[]")
                val contentList = GsonUtils.getGson().fromJson<MutableList<ContentBean>>(contentListStr, object :TypeToken<MutableList<ContentBean>>(){}.type)
                val content = ContentBean(false,etStart.text.toString())
                contentList.add(content)
                putSpValue("content${type}",GsonUtils.getGson().toJson(contentList))
                ToastUtils.showShort("Successfully saved")
                etStart.setText("")
            }
        }
    }

    override fun initData() {
    }
}
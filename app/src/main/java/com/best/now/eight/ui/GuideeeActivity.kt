package com.best.now.eight.ui

import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.best.now.eight.BaseVMActivity
import com.best.now.eight.GuideAdapter
import com.best.now.eight.NextClickCallBack
import com.best.now.eight.R
import com.best.now.eight.databinding.ActivityGuideBinding
import com.best.now.eight.ext.putSpValue

/**
author:zhoujingjin
date:2022/11/19
 */
class GuideeeActivity:BaseVMActivity(), NextClickCallBack {
    private lateinit var adapter: GuideAdapter
    private val binding by binding<ActivityGuideBinding>(R.layout.activity_guide)
    override fun initView() {
        binding.apply {
            viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = GuideAdapter(this@GuideeeActivity)
            viewpager.adapter = adapter
        }
    }

    override fun initData() {
        val imageList = mutableListOf<Int>()
       imageList.add(R.drawable.guide_1)
       imageList.add(R.drawable.guide_2)
       imageList.add(R.drawable.guide_3)
        adapter.setList(imageList)
    }

    override fun clickNext(position: Int) {
        if (position!=2)
        binding.viewpager.setCurrentItem(position+1,true)
        else {
            //记录已经不是第一次进来了
            putSpValue("First",false)
            startActivity(Intent(this,MainnnActivity::class.java).putExtra("first",true))
            finish()
        }
    }
}
package com.best.now.autoclick.ui

import androidx.lifecycle.Observer
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityMainBinding
import com.best.now.autoclick.databinding.ActivityRecordBinding
import com.best.now.autoclick.viewmodel.RecordViewModel
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordConfig

/**
author:zhoujingjin
date:2023/8/9
 */
class RecordActivity:BaseVMActivity() {
    private val binding by binding<ActivityRecordBinding>(R.layout.activity_record)
    private val recordViewModel  = RecordViewModel()
    override fun initView() {
        binding.apply {
            ivRecord.setOnClickListener {
                recordViewModel.recordState.postValue(1)
            }
        }
    }

    override fun initData() {
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.WAV)
        recordViewModel.recordState.observe(this, Observer {
            when(it){
                0->{

                }
                1->{

                }
                3->{

                }
                4->{

                }
            }
        })
    }
}
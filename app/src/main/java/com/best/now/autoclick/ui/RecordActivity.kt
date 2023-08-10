package com.best.now.autoclick.ui

import android.media.MediaPlayer
import android.view.View
import androidx.lifecycle.Observer
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityRecordBinding
import com.best.now.autoclick.ext.getTimeFormat
import com.best.now.autoclick.viewmodel.RecordViewModel
import com.blankj.utilcode.util.ToastUtils
import com.zlw.main.recorderlib.RecordManager
import com.zlw.main.recorderlib.recorder.RecordConfig
import java.io.File


/**
author:zhoujingjin
date:2023/8/9
 */
class RecordActivity:BaseVMActivity() {
    private val binding by binding<ActivityRecordBinding>(R.layout.activity_record)
    private val recordViewModel  = RecordViewModel()
    private val mediaPlayer = MediaPlayer()
    var nowFile:File? = null
    override fun initView() {
        binding.apply {
            ivRecord.setOnClickListener {
                when (recordViewModel.recordState.value) {
                    0 -> {
                        recordViewModel.recordState.postValue(1)
                        RecordManager.getInstance().start()
                    }
                    1 -> {
                        recordViewModel.recordState.postValue(2)
                        RecordManager.getInstance().pause()
                    }
                    2 -> {
                        RecordManager.getInstance().resume()
                    }
                }
            }
            flRerecord.setOnClickListener {
                recordViewModel.recordState.postValue(1)
                RecordManager.getInstance().stop()
                nowFile?.delete()
                RecordManager.getInstance().start()
            }
            ivPlay.setOnClickListener {
                //播放音频
                nowFile?.let {file->
                    if (mediaPlayer.isPlaying){
                        mediaPlayer.stop()
                    }else{
                        mediaPlayer.setDataSource(file.absolutePath)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }
                }
            }
            tvSave.setOnClickListener {
                RecordManager.getInstance().stop()
                ToastUtils.showShort("File saved to:${nowFile?.absolutePath}")
            }
        }
    }

    override fun initData() {
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.WAV)
        RecordManager.getInstance().setRecordResultListener {
            nowFile = it
        }
        recordViewModel.getCurrentSecond().observe(this, Observer {
            binding.tvTime.text =it.getTimeFormat()
        })
        recordViewModel.recordState.observe(this, Observer {
            when(it){
                0->{
                    binding.tvTips.text = "Click to start recording"
                    binding.ivPlay.visibility = View.GONE
                    binding.ivRecord.visibility = View.VISIBLE
                    binding.tv1.visibility = View.GONE
                    binding.tv2.visibility = View.GONE
                    binding.tvSave.visibility = View.GONE
                    binding.flRerecord.visibility = View.GONE
                    binding.ivRecord.setImageResource(R.mipmap.iv_record)
                }
                1->{
                    binding.tvTips.text = "Pause"
                    binding.ivPlay.visibility = View.GONE
                    binding.flRerecord.visibility = View.GONE
                    binding.tv1.visibility = View.GONE
                    binding.tv2.visibility = View.GONE
                    binding.tvSave.visibility = View.GONE
                    binding.ivRecord.setImageResource(R.mipmap.iv_stop)
                }
                2->{
                    binding.tvTips.text = "Continue"
                    binding.ivPlay.visibility = View.VISIBLE
                    binding.tv1.visibility = View.VISIBLE
                    binding.tv2.visibility = View.VISIBLE
                    binding.tvSave.visibility = View.VISIBLE
                    binding.flRerecord.visibility = View.VISIBLE
                    binding.ivRecord.setImageResource(R.mipmap.iv_continue)
                }
                3->{

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer.isPlaying)
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}
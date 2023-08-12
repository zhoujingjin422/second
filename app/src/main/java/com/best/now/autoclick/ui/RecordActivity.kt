package com.best.now.autoclick.ui

import android.media.MediaPlayer
import android.util.Log
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
import com.zlw.main.recorderlib.recorder.RecordHelper
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener
import org.koin.android.ext.android.get
import java.io.File
import java.nio.file.Files


/**
author:zhoujingjin
date:2023/8/9
 */
class RecordActivity:BaseVMActivity() {
    private val binding by binding<ActivityRecordBinding>(R.layout.activity_record)
    private val recordViewModel  = RecordViewModel()
    private var mediaPlayer = MediaPlayer()
    private var nowFile:File? = null
    private var toast = true
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                onBackPressed()
            }
            ivRecord.setOnClickListener {
                when (recordViewModel.recordState.value) {
                    0 -> {
                        recordViewModel.recordState.postValue(1)
                        RecordManager.getInstance().start()
                        recordViewModel.startTimer()
                    }
                    1 -> {
                        recordViewModel.recordState.postValue(2)
                        RecordManager.getInstance().pause()
                        recordViewModel.pauseTimer()
                    }
                    2 -> {
                        recordViewModel.recordState.postValue(1)
                        RecordManager.getInstance().resume()
                        recordViewModel.continueTimer()
                    }
                }
            }
            flRerecord.setOnClickListener {
                toast = false
                recordViewModel.recordState.postValue(1)
                RecordManager.getInstance().stop()
                getNowFile()?.delete()
                RecordManager.getInstance().start()
                recordViewModel.restartTimer()
            }
            ivPlay.setOnClickListener {
                //播放音频
                getNowFile()?.let {file->
                    if (mediaPlayer.isPlaying){
                        mediaPlayer.stop()
                    }else{
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(file.absolutePath)
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                    }
                }
            }
            tvSave.setOnClickListener {
                RecordManager.getInstance().stop()
                recordViewModel.recordState.postValue(0)
                recordViewModel.release()
            }
        }
    }

    private fun getNowFile():File?{
       val file =  File("/storage/emulated/0/Record/")
       val files =  file.listFiles()
        files?.sortBy {
            it.name.substring(7,it.name.length-4).replace("_","").toDouble()
       }
        return if (files.isNullOrEmpty())
            null
        else
            files[files.size-1]
    }
    override fun initData() {
        RecordManager.getInstance().changeFormat(RecordConfig.RecordFormat.MP3)
        RecordManager.getInstance().setRecordResultListener {
            if (toast){
                nowFile = it
                ToastUtils.showShort("File saved to:${nowFile?.absolutePath}")
            }else{
                toast = !toast
            }

        }
        RecordManager.getInstance().setRecordStateListener(object :RecordStateListener{
            override fun onStateChange(state: RecordHelper.RecordState?) {
                Log.e("record",state?.name?:"")

            }

            override fun onError(error: String?) {
                Log.e("record",error?:"")

            }
        })
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
        recordViewModel.release()
    }
}
package com.best.now.six.ui

import androidx.recyclerview.widget.GridLayoutManager
import com.best.now.six.BaseVMActivity
import com.best.now.six.R
import com.best.now.six.adapter.NoiseAdapter
import com.best.now.six.bean.NoiseBean
import com.best.now.six.databinding.ActivityNoiseBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


/**
author:zhoujingjin
date:2023/9/3
 */
class NoiseActivity:BaseVMActivity() {
    private val binding by binding<ActivityNoiseBinding>(R.layout.activity_noise)
    private lateinit var noiseAdapter: NoiseAdapter
    private val data = mutableListOf<NoiseBean>()
    private val strArr = mutableListOf("White Noise", "Fan", "Pink Noise", "Hot Wind"
        , "Thunder","Sea Wave","Waterfall","Backwash","Heavy Rain","Breeze","Mountain","Light Rain")
    private val soundArr = mutableListOf("白噪音.mp3", "风扇.mp3", "粉色噪音.mp3", "炽热风浪.mp3"
        , "电闪雷鸣.mp3","海浪轻抚.mp3","近闻瀑布.mp3","巨浪涛天.mp3","倾盆大雨.mp3","清风拂面.mp3","深林山风.mp3","小雨淅沥.mp3")
    private val resIdArr = mutableListOf(R.drawable.guide_1, R.drawable.guide_1,R.drawable.guide_1, R.drawable.guide_1
        , R.drawable.guide_1,R.drawable.guide_1,R.drawable.guide_1,R.drawable.guide_1,R.drawable.guide_1,
        R.drawable.guide_1,R.drawable.guide_1,R.drawable.guide_1)
    private var isPlaying = false
    private var nowPlay:NoiseBean? = null
    override fun initView() {
        binding.apply{
            recyclerView.layoutManager = GridLayoutManager(this@NoiseActivity,3)
            noiseAdapter = NoiseAdapter {
                var newPlay = false
                if (nowPlay==it){
                    isPlaying = !isPlaying
                    newPlay = false
                }else{
                    nowPlay = it
                    isPlaying = true
                    newPlay = true
                }
                playSound(newPlay)
            }
            recyclerView.adapter = noiseAdapter
        }
    }

   private  var player:ExoPlayer? = null
   private fun playSound(newPlay: Boolean) {
       if (player==null){
           player = ExoPlayer.Builder(this).build()
       }
       if (isPlaying){
           if (newPlay){
               val mediaItem: MediaItem = MediaItem.fromUri("http://pdf3.vfttuuw.top/jingluo/${nowPlay?.sound}")
               player?.setMediaItem(mediaItem)
               player?.prepare()
               player?.playWhenReady = true
           }else{
               player?.playWhenReady = true
           }
       }else{
           player?.pause()
       }


    }
    override fun initData() {
        for(i in 0..11){
            data.add(NoiseBean(strArr[i],resIdArr[i],soundArr[i]))
        }
        noiseAdapter.setNewInstance(data)
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}
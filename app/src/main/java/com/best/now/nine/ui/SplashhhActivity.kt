package com.best.now.nine.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.best.now.nine.R
import com.best.now.nine.ext.getSpValue

/**
author:zhoujingjin
date:2022/11/18
 */
class SplashhhActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        if (!isTaskRoot) {
            finish()
            return
        }
        //延迟两秒，判断是不是首次进入，首次进入到导航页，不是直接进首页
        findViewById<ConstraintLayout>(R.id.parent).postDelayed({
            if (getSpValue("First",true)){
                startActivity(Intent(this,GuideeeActivity::class.java))
            }else startActivity(Intent(this,MainnnActivity::class.java))
            finish()
        },1000L)
    }
}
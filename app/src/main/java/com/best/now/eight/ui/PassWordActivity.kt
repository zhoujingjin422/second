package com.best.now.eight.ui

import android.content.Intent
import android.text.format.DateUtils
import android.view.Gravity
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.best.now.eight.BaseVMActivity
import com.best.now.eight.R
import com.best.now.eight.databinding.ActivityPassWordBinding
import com.best.now.eight.ext.getSpValue
import com.best.now.eight.ext.hideKeyboard
import com.best.now.eight.ext.putSpValue
import com.best.now.eight.ext.showKeyboard
import com.blankj.utilcode.util.ToastUtils
import com.google.android.gms.common.util.DataUtils

/**
author:zhoujingjin
date:2023/9/16
 */
class PassWordActivity:BaseVMActivity() {
    private val binding by binding<ActivityPassWordBinding>(R.layout.activity_pass_word)
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            et.isCursorVisible = false
            tvCancel.setOnClickListener { finish() }
          val setPassWord = intent.getBooleanExtra("setpassword",false)
          val change = intent.getBooleanExtra("change",false)
            et.addTextChangedListener {
                when(it.toString().length){
                    0->{
                        tv1.text = ""
                        tv2.text = ""
                        tv3.text = ""
                        tv4.text = ""
                    }
                    1->{
                        tv1.text = "*"
                        tv2.text = ""
                        tv3.text = ""
                        tv4.text = ""
                    }
                    2->{
                        tv1.text = "*"
                        tv2.text = "*"
                        tv3.text = ""
                        tv4.text = ""
                    }
                    3->{
                        tv1.text = "*"
                        tv2.text = "*"
                        tv3.text = "*"
                        tv4.text = ""
                    }
                    4->{
                        tv1.text = "*"
                        tv2.text = "*"
                        tv3.text = "*"
                        tv4.text = "*"
                        if (setPassWord||change){
                            putSpValue("password",it.toString())
                           val toast =  Toast.makeText(this@PassWordActivity,"save successful",Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.CENTER,0,0)
                            toast.show()
                            if (setPassWord){
                                startActivity(Intent(this@PassWordActivity,JournalListActivity::class.java))
                                finish()
                            }else{
                                finish()
                            }
                        } else{
                            if (it.toString() == getSpValue("password", "")){
                                startActivity(Intent(this@PassWordActivity,JournalListActivity::class.java))
                                finish()
                            }else{
                                val toast =  Toast.makeText(this@PassWordActivity,"password is incorrect,please try again",Toast.LENGTH_SHORT)
                                toast.setGravity(Gravity.CENTER,0,0)
                                toast.show()
                                et.setText("")
                            }

                        }
                    }
                }

            }
        }
    }

    override fun initData() {
        showKeyboard(binding.et)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideKeyboard()
    }
}
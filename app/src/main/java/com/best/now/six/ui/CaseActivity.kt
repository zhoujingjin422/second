package com.best.now.six.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import com.best.now.six.BaseVMActivity
import com.best.now.six.R
import com.best.now.six.databinding.ActivityCaseBinding
import com.best.now.six.dialog.SavePop
import com.best.now.six.ext.copyToClipboard

/**
author:zhoujingjin
date:2023/9/3
 */
class CaseActivity:BaseVMActivity() {
    private val binding by binding<ActivityCaseBinding>(R.layout.activity_case)
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvUp.setOnClickListener {
                //都变成大写
                etInput.setText(etInput.text.toString().uppercase())
            }
            tvLow.setOnClickListener {
                //都变成小写
                etInput.setText(etInput.text.toString().lowercase())
            }
            tvCap.setOnClickListener {
                //第一个字母大写
                etInput.setText(capitalizeWords(etInput.text.toString()))
            }
            tvClear.setOnClickListener {
                etInput.setText("")
            }
            tvRemove.setOnClickListener {
                etInput.setText(etInput.text.toString().replace(" ",""))
            }
            tvCopy.setOnClickListener {
                //copy
                copyToClipboard(etInput.text.toString())
                SavePop(this@CaseActivity).showPopupWindow()
            }
        }
    }
   private fun capitalizeWords(input: String): String {
        val words = input.split(" ")
        val capitalizedWords = words.map { it.capitalize() }
        return capitalizedWords.joinToString(" ")
    }
    override fun initData() {
    }
}
package com.best.now.autoclick.ui

import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityTextReplaceBinding
import com.best.now.autoclick.ext.copyToClipboard
import com.blankj.utilcode.util.ToastUtils

class TextReplaceActivity:BaseVMActivity() {
    private val binding by binding<ActivityTextReplaceBinding>(R.layout.activity_text_replace)
    override fun initView() {
        binding.apply {
            tvReplace.setOnClickListener {
                if (etStart.text.toString().isEmpty()){
                    ToastUtils.showShort("Please enter the content that needs to be replaced")
                    return@setOnClickListener
                }
                if (etMiddle.text.toString().isEmpty()){
                    ToastUtils.showShort("Please enter the characters that needs to be replaced")
                    return@setOnClickListener
                }
                if (etMiddle2.text.toString().isEmpty()){
                    ToastUtils.showShort("Please enter the replaced characters")
                    return@setOnClickListener
                }
               val string = etStart.text.toString().replace(etMiddle.text.toString(),etMiddle2.text.toString())
                etBottom.setText(string)
            }
            tvCopy.setOnClickListener {
                copyToClipboard(etStart.text.toString())
                ToastUtils.showShort("Copied successfully")
            }
            tvEmpty.setOnClickListener {
                etStart.setText("")
                etMiddle.setText("")
                etMiddle2.setText("")
                etBottom.setText("")
            }
            toolBar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    override fun initData() {
    }
}
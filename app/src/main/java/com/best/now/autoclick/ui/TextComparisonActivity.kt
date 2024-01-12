package com.best.now.autoclick.ui

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityTextComparisonBinding
import com.best.now.autoclick.databinding.ActivityTextReplaceBinding
import com.blankj.utilcode.util.ToastUtils

class TextComparisonActivity:BaseVMActivity() {
    private val binding by binding<ActivityTextComparisonBinding>(R.layout.activity_text_comparison)
    override fun initView() {
        binding.apply {
            tvComparison.setOnClickListener {
                if (etBottom.text.toString().isEmpty()||etStart.text.toString().isEmpty()){
                    ToastUtils.showShort("Please enter text content")
                    return@setOnClickListener
                }
                highlightDifferentText(etBottom,etStart)
            }
            tvClear.setOnClickListener {
                etBottom.setText("")
                etStart.setText("")
            }
            toolBar.setNavigationOnClickListener {
                finish()
            }
        }
    }

    override fun initData() {
    }

    private fun highlightDifferentText(view1: TextView, view2: TextView) {
        val text1 = view1.text.toString()
        val text2 = view2.text.toString()

        val maxLength = text1.length.coerceAtLeast(text2.length)
        val spannableString1 = SpannableString(text1)
        val spannableString2 = SpannableString(text2)

        for (i in 0 until maxLength) {
            if (i<view1.text.length&&i<view2.text.length){
                if (text1[i] != text2[i]) {
                    // 设置不同文字的背景色和文字颜色
                    spannableString1.setSpan(
                        BackgroundColorSpan(resources.getColor(R.color.c_ff755f)),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    spannableString1.setSpan(
                        ForegroundColorSpan(Color.WHITE),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    spannableString2.setSpan(
                        BackgroundColorSpan(resources.getColor(R.color.c_ff755f)),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    spannableString2.setSpan(
                        ForegroundColorSpan(Color.WHITE),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else if (i >=text1.length){
                spannableString2.setSpan(
                    BackgroundColorSpan(resources.getColor(R.color.c_ff755f)),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                spannableString2.setSpan(
                    ForegroundColorSpan(Color.WHITE),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }else if (i >=text2.length){
                spannableString1.setSpan(
                    BackgroundColorSpan(resources.getColor(R.color.c_ff755f)),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                spannableString1.setSpan(
                    ForegroundColorSpan(Color.WHITE),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        // 将修改后的SpannableString设置给TextView
        view1.text = spannableString1
        view2.text = spannableString2
    }


}
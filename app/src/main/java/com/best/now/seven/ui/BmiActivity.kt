package com.best.now.seven.ui

import com.best.now.seven.BaseVMActivity
import com.best.now.seven.R
import com.best.now.seven.databinding.ActivityBmiBinding
import com.best.now.seven.dialog.BmiPop
import com.lsp.RulerView
import java.text.DecimalFormat


/**
author:zhoujingjin
date:2023/9/3
 */
class BmiActivity:BaseVMActivity() {
    private val binding by binding<ActivityBmiBinding>(R.layout.activity_bmi)
    override fun initView() {
        binding.apply{
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvCalculate.setOnClickListener {
                val result = pb1.progress/((pb2.progress/100f)*(pb2.progress/100f))
                val decimalFormat = DecimalFormat("#.#")
                val bmiPop = BmiPop(this@BmiActivity, decimalFormat.format(result))
                bmiPop.showPopupWindow()
            }
            rulerView.apply {
               setUnit("kg")
               setShowScaleResult(false)
                setMaxScale(20)
                setMinScale(0)
                setScaleCount(10)
                setScaleLimit(10)
                setScaleNumColor(resources.getColor(R.color.cff5562))
                setFirstScale(5f)
            }
            rulerView.setOnChooseResulterListener(object :RulerView.OnChooseResulterListener{
                override fun onEndResult(result: String?) {
                    result?.let {
                        tv2.text = "${it.toFloat().toInt()}kg"
                        pb1.progress = it.toFloat().toInt()
                    }
                }

                override fun onScrollResult(result: String?) {
                    result?.let {
                        tv2.text = "${it.toFloat().toInt()}kg"
                        pb1.progress = it.toFloat().toInt()
                    }
                }
            })
            rulerView2.apply {
                setUnit("cm")
                setShowScaleResult(false)
                setMaxScale(24)
                setScaleCount(10)
                setScaleLimit(10)
                setMinScale(0)
                setScaleNumColor(resources.getColor(R.color.cff5562))
                setFirstScale(17f)
            }
            rulerView2.setOnChooseResulterListener(object :RulerView.OnChooseResulterListener{
                override fun onEndResult(result: String?) {
                    result?.let {
                        tv4.text = "${it.toFloat().toInt()}cm"
                        pb2.progress = it.toFloat().toInt()
                    }
                }

                override fun onScrollResult(result: String?) {
                    result?.let {
                        tv4.text = "${it.toFloat().toInt()}cm"
                        pb2.progress = it.toFloat().toInt()
                    }

                }
            })

        }
    }
    override fun initData() {

    }
}
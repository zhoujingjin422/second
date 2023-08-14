package com.best.now.autoclick.ui

import android.app.DatePickerDialog
import android.view.View
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.bean.ProductBean
import com.best.now.autoclick.databinding.ActivityProductBinding
import com.best.now.autoclick.dialog.ChooseTypePop
import com.best.now.autoclick.ext.getSpValue
import com.best.now.autoclick.ext.putSpValue
import com.best.now.autoclick.viewmodel.ProductViewModel
import com.google.gson.Gson
import java.util.Calendar

/**
author:zhoujingjin
date:2023/8/13
 */
class ProductActivity:BaseVMActivity() {
    private val binding by binding<ActivityProductBinding>(R.layout.activity_product)
    private lateinit var viewModel: ProductViewModel
    private var date =  Calendar.getInstance()
    private var type = 1
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            tvDate.setOnClickListener {
                //日期选择
                val datePicker = DatePickerDialog(this@ProductActivity,{_,year,month,day->
                    tvDate.text = "$year-${month+1}-$day"
                    date.set(year,month,day)
                },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
            tvDay.setOnClickListener {
                val pop = ChooseTypePop(this@ProductActivity) {
                    type = it
                    tvDay.text = when (it) {
                        1 -> "Day"
                        2 -> "Month"
                        3 -> "Year"
                        else -> {
                            "Day"
                        }
                    }
                }
                pop.showPopupWindow()
            }
            tvCalculate.setOnClickListener {
                val temp = ProductBean(etInput.editableText.toString().toInt(),type,date.timeInMillis)
                viewModel.productBean.postValue(temp)
                putSpValue("product",Gson().toJson(temp))
            }
            tvReset.setOnClickListener {
                //清理
                putSpValue("product","")
                viewModel.productBean.postValue(null)
            }
        }
        viewModel = ProductViewModel()
    }

    override fun initData() {
        val str = getSpValue("product","")
        if (str.isNotEmpty()){
           val productBean = Gson().fromJson(str,ProductBean::class.java)
            viewModel.productBean.postValue(productBean)
        }else{
            viewModel.productBean.postValue(null)
        }
        viewModel.productBean.observe(this){
            if (it==null){
                binding.cardBottom.visibility = View.GONE
                date = Calendar.getInstance()
                binding.etInput.setText("")
                binding.tvDate.text = "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH)+1}-${date.get(Calendar.DAY_OF_MONTH)}"
                binding.tvDay.text ="Day"
            }else{
                binding.cardBottom.visibility = View.VISIBLE
                binding.tvDay.text = when(it.type){
                    1->  "Day"
                    2->  "Month"
                    3->  "Year"
                    else -> {"Day"}
                }
                binding.etInput.setText(it.num.toString())
                //开始算时间吧
                val now = Calendar.getInstance()
                now.timeInMillis = it.startDay
                binding.tvDate.text = "${now.get(Calendar.YEAR)}-${now.get(Calendar.MONTH)+1}-${now.get(Calendar.DAY_OF_MONTH)}"
                var day = 0
                when(it.type){
                    1->{
                        day = it.num
                        now.timeInMillis = it.startDay+day*24*3600*1000L
                        val year =  now.get(Calendar.YEAR)
                        val month =  now.get(Calendar.MONTH)+1
                        val dayStr =  now.get(Calendar.DAY_OF_MONTH)
                        binding.tvEndDay.text = "${year}Year${month}Mon${dayStr}Day"
                    }
                    2->{
                        val month =  now.get(Calendar.MONTH)+1
                        val dayStr =  now.get(Calendar.DAY_OF_MONTH)
                        val year = now.get(Calendar.YEAR)+(it.num+month)/12
                        binding.tvEndDay.text = "${year}Year${(it.num+month)%12}Mon${dayStr}Day"
                        now.set(year,(it.num+month)%12-1,dayStr)
                    }
                    3->{
                        val year =  now.get(Calendar.YEAR)+it.num
                        val month =  now.get(Calendar.MONTH)+1
                        val dayStr =  now.get(Calendar.DAY_OF_MONTH)
                        now.set(year,month-1,dayStr)
                        binding.tvEndDay.text = "${year}Year${month}Mon${dayStr}Day"
                    }
                }
                if (now.timeInMillis<Calendar.getInstance().timeInMillis){
                    //过期了
                    binding.tvTips.text = "Be past its shelf life"
                    binding.tvTips.setTextColor(resources.getColor(R.color.F7726C))
                }else{
                    binding.tvTips.text = "Within the shelf life"
                    binding.tvTips.setTextColor(resources.getColor(R.color.c_00A375))
                }
            }
        }
    }
}
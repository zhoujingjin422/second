package com.best.now.autoclick.ui

import android.app.DatePickerDialog
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.bean.ProductBean
import com.best.now.autoclick.databinding.ActivityProductBinding
import com.best.now.autoclick.databinding.ActivityWeightRecordBinding
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
    private val date =  Calendar.getInstance()
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
                val pop = ChooseTypePop(this@ProductActivity){
                    type = it
                }
                pop.showPopupWindow()
            }
            etInput.addTextChangedListener {

            }
            tvCalculate.setOnClickListener {
                val temp = ProductBean(etInput.editableText.toString().toInt(),type,date)
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
        }
        viewModel.productBean.observe(this){
            if (it==null){
                binding.cardAdd.visibility = View.GONE
            }else{
                binding.cardAdd.visibility = View.VISIBLE

            }
        }
    }
}
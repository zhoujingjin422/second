package com.best.now.autoclick.ui

import android.app.DatePickerDialog
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.adapter.WeightAdapter
import com.best.now.autoclick.bean.WeightBean
import com.best.now.autoclick.databinding.ActivityWeightRecordBinding
import com.best.now.autoclick.ext.getSpValue
import com.best.now.autoclick.ext.putSpValue
import com.best.now.autoclick.viewmodel.WeightRecordViewModel
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Calendar

/**
author:zhoujingjin
date:2023/8/13
 */
class WeightRecordActivity:BaseVMActivity() {
    private val binding by binding<ActivityWeightRecordBinding>(R.layout.activity_weight_record)
    private lateinit var viewModel:WeightRecordViewModel
    private lateinit var adapter: WeightAdapter
    private val date =  Calendar.getInstance()
    override fun initView() {
        viewModel = WeightRecordViewModel()
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            recyclerView.layoutManager = LinearLayoutManager(this@WeightRecordActivity)
            adapter = WeightAdapter{ data->
                viewModel.data.postValue(data)
                if (data.size==0){
                    putSpValue("Weight","")
                }else putSpValue("Weight",Gson().toJson(data))
            }
            recyclerView.adapter = adapter
            tvStart.setOnClickListener {
                viewModel.isEdit.postValue(true)
            }
            ivAdd.setOnClickListener {
                viewModel.isEdit.postValue(true)
            }
            tvSave.setOnClickListener {
                if (etInput.editableText.toString().isEmpty()){
                    ToastUtils.showShort("请输入体重")
                }
                val data = viewModel.data.value
                data?.add(0,WeightBean("${date.get(Calendar.YEAR)}/${date.get(Calendar.MONTH)+1}","${date.get(Calendar.MONTH)+1}/${date.get(Calendar.DAY_OF_MONTH)}",etInput.editableText.toString()))
                viewModel.data.postValue(data)
                viewModel.isEdit.postValue(false)
                putSpValue("Weight",Gson().toJson(viewModel.data.value))
            }
            tvDate.text = "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH)+1}-${date.get(Calendar.DAY_OF_MONTH)}"
            tvDate.setOnClickListener {
                //日期选择
                val datePicker = DatePickerDialog(this@WeightRecordActivity,{_,year,month,day->
                    tvDate.text = "$year-${month+1}-$day"
                    date.set(year,month,day)
                },date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DAY_OF_MONTH))
                datePicker.show()
            }
        }
    }

    override fun initData() {
       val weightStr =  getSpValue("Weight","")
        if (weightStr.isNotEmpty()){
          val weightList =  Gson().fromJson<List<WeightBean>>(weightStr,object :TypeToken<List<WeightBean>>(){}.type)
            viewModel.data.postValue(weightList as MutableList<WeightBean>?)
        }
        viewModel.isEdit.observe(this){
            if (it){
                binding.cardAdd.visibility = View.VISIBLE
                binding.tvSave.visibility = View.VISIBLE
                binding.ivAdd.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.tvStart.visibility = View.GONE
            }else{
                binding.cardAdd.visibility = View.GONE
                binding.etInput.setText("")
                binding.tvSave.visibility = View.GONE
                binding.ivAdd.visibility = View.VISIBLE
                if (viewModel.data.value.isNullOrEmpty()){
                    binding.tvStart.visibility = View.VISIBLE
                }else{
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }
        viewModel.data.observe(this){
            adapter.setList(it)
            if (it.isNullOrEmpty()){
                binding.tvStart.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else{
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }
}
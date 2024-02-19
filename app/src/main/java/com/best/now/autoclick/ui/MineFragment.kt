package com.best.now.autoclick.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.FragmentMineBinding
import com.best.now.autoclick.utils.Constant

class MineFragment:Fragment() {
    private var binding:FragmentMineBinding? = null
    companion object {
        //MineFragment
        fun getInstance(): MineFragment {
            return MineFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_mine,null)
         binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            ivPrivate.setOnClickListener {
                WebActivity.startActivity(
                    requireActivity(),
                    "隐私政策",
                    Constant.URL_PRIVACY_POLICY
                )
            }
            ivServe.setOnClickListener {
                    WebActivity.startActivity(
                        requireActivity(),
                        "服务条款",
                        Constant.URL_TERMS_OF_USE
                    )
            }
        }
    }
}
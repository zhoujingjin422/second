package com.best.now.autoclick.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.FragmentListenBinding
import com.best.now.autoclick.databinding.FragmentResourceBinding

class ResourceFragment:Fragment() {
    private var binding: FragmentResourceBinding? = null
    companion object {
        fun getInstance(): ResourceFragment {
            return ResourceFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_resource,null)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            iv1.setOnClickListener {
                WebPlayPianoActivity.startActivity(requireActivity(),"","http://haosi.ayhhhrk.cn/english/android/haosi/#/resourse?categoryId=51&lessonId=1")
            }
            iv2.setOnClickListener {
                WebPlayPianoActivity.startActivity(requireActivity(),"","http://haosi.ayhhhrk.cn/english/android/haosi/#/resourse?categoryId=52&lessonId=2")
            }
            iv3.setOnClickListener {
                WebPlayPianoActivity.startActivity(requireActivity(),"","http://haosi.ayhhhrk.cn/english/android/haosi/#/resourse?categoryId=53&lessonId=3")
            }
            iv4.setOnClickListener {
                WebPlayPianoActivity.startActivity(requireActivity(),"","http://haosi.ayhhhrk.cn/english/android/haosi/#/resourse?categoryId=54&lessonId=4")
            }
        }
    }
}
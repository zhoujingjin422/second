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

class ListenFragment:Fragment() {
    private var binding: FragmentListenBinding? = null
    companion object {
        fun getInstance(): ListenFragment {
            return ListenFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_listen,null)
        binding = DataBindingUtil.bind(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding?.apply {
            ivShare.setOnClickListener {
                startActivity(Intent(requireContext(),ShareActivity::class.java))
            }
            webView.setOnClickListener {
                WebPlayPianoActivity.startActivity(requireActivity(),"","http://haosi.ayhhhrk.cn/english/android/haosi/#/")
            }
        }
    }
}
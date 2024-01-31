package com.best.now.autoclick.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.adapter.TodoAdapter
import com.best.now.autoclick.adapter.TodoAdapter2
import com.best.now.autoclick.adapter.TodoAdapter3
import com.best.now.autoclick.adapter.TodoAdapter4
import com.best.now.autoclick.bean.ContentBean
import com.best.now.autoclick.databinding.ActivityQuadrantListBinding
import com.best.now.autoclick.ext.getSpValue
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken


class QuadrantListActivity: BaseVMActivity() {
    private val binding by binding<ActivityQuadrantListBinding>(R.layout.activity_quadrant_list)
    private var initialX = 0f
    private  var initialY= 0f
    private var crossAreaOriginalLeft = 0
    private var crossAreaOriginalTop = 0
    private var adapter1:TodoAdapter? = null
    private var adapter2:TodoAdapter2? = null
    private var adapter3:TodoAdapter3? = null
    private var adapter4:TodoAdapter4? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        binding.apply {
            setSupportActionBar(toolBar)
            toolBar.setNavigationOnClickListener {
                finish()
            }
            fab.setOnClickListener {
                startActivity(Intent(this@QuadrantListActivity,NewToDoContentActivity::class.java))
            }
            crossArea.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = event.rawX
                        initialY = event.rawY
                        crossAreaOriginalLeft = crossArea.left
                        crossAreaOriginalTop = crossArea.top
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val deltaX = event.rawX - initialX
                        val deltaY = event.rawY - initialY
                        val newLeft = (crossAreaOriginalLeft + deltaX).toInt()
                        val newTop = (crossAreaOriginalTop + deltaY).toInt()
                        val newRight = newLeft + crossArea.width
                        val newBottom = newTop + crossArea.height

                        // Update crossArea position
                        crossArea.layout(newLeft, newTop, newRight, newBottom)

                        // Update topLeftLayout
                        val parentWidth = (crossArea.parent as ConstraintLayout).width
                        val parentHeight = (crossArea.parent as ConstraintLayout).height

                        val topLeftLayoutParams = topLeftLayout.layoutParams as ConstraintLayout.LayoutParams
                        topLeftLayoutParams.matchConstraintPercentWidth = newLeft.toFloat() / parentWidth.toFloat()
                        topLeftLayoutParams.matchConstraintPercentHeight = newTop.toFloat() / parentHeight.toFloat()
                        topLeftLayout.layoutParams = topLeftLayoutParams
//                        topLeftLayout.layout(0, 0, newLeft, newTop)
//                        topLeftLayout.invalidate()
//                        for (i in 0 until topLeftLayout.childCount){
//                           val tv1 = topLeftLayout.getChildAt(i) as TextView
////                            topLeftLayout.getChildAt(i).layout(0, 0, newLeft, topLeftLayout.getChildAt(i).height)
//                            tv1.text = "Important and urgent"
//                        }
                        // Update topRightLayout
                        val topRightLayoutParams = topRightLayout.layoutParams as ConstraintLayout.LayoutParams
                        topRightLayoutParams.matchConstraintPercentWidth = (parentWidth-newRight).toFloat() / parentWidth.toFloat()
                        topRightLayoutParams.matchConstraintPercentHeight = topLeftLayoutParams.matchConstraintPercentHeight
                        topRightLayout.layoutParams = topRightLayoutParams
//                        topRightLayout.layout(newRight,  0, parentWidth, newTop)

                        // Update bottomLeftLayout
                        val bottomLeftLayoutParams = bottomLeftLayout.layoutParams as ConstraintLayout.LayoutParams
                        bottomLeftLayoutParams.matchConstraintPercentWidth = topLeftLayoutParams.matchConstraintPercentWidth
                        bottomLeftLayoutParams.matchConstraintPercentHeight = (parentHeight-newBottom).toFloat() / parentHeight.toFloat()
//                        bottomLeftLayout.layout(0, newBottom, newLeft, parentHeight)
                        bottomLeftLayout.layoutParams = bottomLeftLayoutParams

                        // Update bottomRightLayout
                        val bottomRightLayoutParams = bottomRightLayout.layoutParams as ConstraintLayout.LayoutParams
                        bottomRightLayoutParams.matchConstraintPercentWidth = topRightLayoutParams.matchConstraintPercentWidth
                        bottomRightLayoutParams.matchConstraintPercentHeight = bottomLeftLayoutParams.matchConstraintPercentHeight
//                        bottomRightLayout.layout(newRight, newBottom, parentWidth, parentHeight)
                        bottomRightLayout.layoutParams = bottomRightLayoutParams

                    }
                }
                true
            }
            rvIu.layoutManager = LinearLayoutManager(this@QuadrantListActivity)
            rvInu.layoutManager = LinearLayoutManager(this@QuadrantListActivity)
            rvUni.layoutManager = LinearLayoutManager(this@QuadrantListActivity)
            rvNuni.layoutManager = LinearLayoutManager(this@QuadrantListActivity)
            adapter1 = TodoAdapter(1)
            adapter2 = TodoAdapter2(2)
            adapter3 = TodoAdapter3(3)
            adapter4 = TodoAdapter4(4)
            rvIu.adapter = adapter1
            rvInu.adapter = adapter2
            rvUni.adapter = adapter3
            rvNuni.adapter = adapter4
        }
    }

    override fun initData() {
        val contentListStr1 = getSpValue("content1","[]")
        val contentListStr2 = getSpValue("content2","[]")
        val contentListStr3 = getSpValue("content3","[]")
        val contentListStr4 = getSpValue("content4","[]")
        val contentList1 = GsonUtils.getGson().fromJson<MutableList<ContentBean>>(contentListStr1, object :
            TypeToken<MutableList<ContentBean>>(){}.type)
        val contentList2 = GsonUtils.getGson().fromJson<MutableList<ContentBean>>(contentListStr2, object :
            TypeToken<MutableList<ContentBean>>(){}.type)
        val contentList3 = GsonUtils.getGson().fromJson<MutableList<ContentBean>>(contentListStr3, object :
            TypeToken<MutableList<ContentBean>>(){}.type)
        val contentList4 = GsonUtils.getGson().fromJson<MutableList<ContentBean>>(contentListStr4, object :
            TypeToken<MutableList<ContentBean>>(){}.type)
        adapter1?.setNewInstance(contentList1)
        adapter2?.setNewInstance(contentList2)
        adapter3?.setNewInstance(contentList3)
        adapter4?.setNewInstance(contentList4)
    }

    override fun onResume() {
        super.onResume()
        initData()
    }
}
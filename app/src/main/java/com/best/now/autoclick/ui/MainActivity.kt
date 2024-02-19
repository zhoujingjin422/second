package com.best.now.autoclick.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
//import com.android.billingclient.api.*
import com.best.now.autoclick.BaseVMActivity
import com.best.now.autoclick.R
import com.best.now.autoclick.databinding.ActivityMainBinding
import com.best.now.autoclick.dialog.ServeAndPrivatePop
import com.best.now.autoclick.ext.getSpValue
import com.best.now.autoclick.utils.ActionHelper
import com.best.now.autoclick.utils.Constant
import com.best.now.autoclick.utils.ImageUtils
//import com.best.now.autoclick.utils.InPurchaseUtils
//import com.best.now.autoclick.utils.adParentList
//import com.best.now.autoclick.utils.isPurchased
//import com.best.now.autoclick.utils.loadAd
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : BaseVMActivity() {
    companion object {
        var purchased = false
        var purchaseTime = 0L
        var productId = ""
        const val BUS_TAG_BUY_STATE_PURCHASED = "BUS_TAG_BUY_STATE_PURCHASED"
//        lateinit var  inPurchaseUtils : InPurchaseUtils
    }
    private val fragmentList = mutableMapOf<Int,Fragment>()
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    @SuppressLint("SuspiciousIndentation")
    override fun initView() {
        binding.apply {
            iv1.setOnClickListener {
                changeViewState(1)
            }
            iv2.setOnClickListener {
                changeViewState(2)
            }
            iv3.setOnClickListener {
                changeViewState(3)
            }
            iv4.setOnClickListener {
                changeViewState(4)
            }

        }
//        inPurchaseUtils = InPurchaseUtils(this)
//        inPurchaseUtils.conListener = object :InPurchaseUtils.ConnectListener{
//            override fun connectSuc() {
//                inPurchaseUtils.queryPurchases()
//            }
//        }
    }

    private fun changeViewState(i: Int) {
        when (i) {
            1 -> {
                binding.iv1.setImageResource(R.drawable.home_true)
                binding.iv2.setImageResource(R.drawable.listen_false)
                binding.iv3.setImageResource(R.drawable.res_false)
                binding.iv4.setImageResource(R.drawable.mine_false)
            }

            2 -> {
                binding.iv1.setImageResource(R.drawable.home_false)
                binding.iv2.setImageResource(R.drawable.listen_true)
                binding.iv3.setImageResource(R.drawable.res_false)
                binding.iv4.setImageResource(R.drawable.mine_false)
            }

            3 -> {
                binding.iv1.setImageResource(R.drawable.home_false)
                binding.iv2.setImageResource(R.drawable.listen_false)
                binding.iv3.setImageResource(R.drawable.res_true)
                binding.iv4.setImageResource(R.drawable.mine_false)
            }

            4 -> {
                binding.iv1.setImageResource(R.drawable.home_false)
                binding.iv2.setImageResource(R.drawable.listen_false)
                binding.iv3.setImageResource(R.drawable.res_false)
                binding.iv4.setImageResource(R.drawable.mine_true)
            }
        }
        changeFragment(i)
    }
    private fun changeFragment(i:Int){
       val trans =  supportFragmentManager.beginTransaction()
       var fragment =  fragmentList[i]
        fragmentList.forEach{
            trans.hide(it.value)
        }
        if (fragment==null){
            fragment = when(i){
                1->{
                    WebFragment.getInstance()
                }

                2->{
                    ListenFragment.getInstance()
                }

                3->{
                    ResourceFragment.getInstance()
                }
                else->{
                    MineFragment.getInstance()
                }
            }
            trans.add(R.id.container,fragment).commitAllowingStateLoss()
            fragmentList[i] = fragment
        }else{
            trans.show(fragment).commitAllowingStateLoss()
        }
    }

    override fun initData() {
        if (!getSpValue("hasShowPrivacy",false)){
            ServeAndPrivatePop(this).showPopupWindow()
        }
        changeFragment(1)
        ActionHelper.doAction("open")
//        loadAd(binding.advBanner)
//        if (intent.getBooleanExtra("first",false)&&!purchased){
//            startActivity(Intent(this,SubscribeActivity::class.java))
//        }
    }
//    @BusUtils.Bus(tag = BUS_TAG_BUY_STATE_PURCHASED)
//    fun purchase(purchase: Purchase) {
//        purchased = true
//        purchaseTime = purchase.purchaseTime
//        productId = GsonUtils.toJson(purchase.skus)
////        ActionHelper.doAction("buy_success")
//
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2001) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { // 用户成功授予权限

            } else {
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
//                    ToastUtils.showShort("请到设置中同意存储权限")
//                } else {
//                    ToastUtils.showShort("请务必同意存储权限")
//                }
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        BusUtils.register(this)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        BusUtils.unregister(this)
//    }
}
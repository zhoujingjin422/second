package com.best.now.pdf.utils;

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.android.billingclient.api.*
import com.best.now.pdf.BaseVMActivity
import com.best.now.pdf.ui.MainnnActivity
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.ToastUtils

class InPurchaseUtils {


    interface ConnectListener {
        fun connectSuc()
    }

    var skuDetailsList = arrayListOf<SkuDetails>()
    lateinit var billingClient: BillingClient
    lateinit var purchasesUpdatedListener: PurchasesUpdatedListener
    lateinit var billingClientStateListener: BillingClientStateListener
    lateinit var acknowledgePurchaseResponseListener: AcknowledgePurchaseResponseListener
    lateinit var conListener: ConnectListener
    var temPurchase: Purchase? = null

    var connectionNum = 0//连接次数，连接失败时使用

    lateinit var context: BaseVMActivity

    var selectVipID: String? = null

    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 0) {
                clientConnection()
            } else if (msg.what == 1) {
                querySku(false)
            }
        }
    }



    var purchaseTime = 0L//购买时间

    constructor(context: BaseVMActivity) {
        this.context = context

        setListener()
        init()
    }



    /*** 设置监听 */
    private fun setListener() {

        //购买更新的侦听器
        purchasesUpdatedListener =
                PurchasesUpdatedListener { billingResult, purchases ->
//                    Log.e(
//                            "PurchasesUpdatedListener:" + GsonUtils.toJson(billingResult)
//                                    + "===>" + GsonUtils.toJson(purchases)
//                    )

                    //购买结果监听
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
                        var purchased = false
                        //购买成功
                        for (purchase in purchases) {
                            // Verify the purchase.
                            // Ensure entitlement was not already granted for this purchaseToken.
                            // Grant entitlement to the user.

                            //验证购买情况。//确保尚未为此purchaseToken授予权限。//授予用户权限。
                            when (purchase.purchaseState) {
                                Purchase.PurchaseState.PENDING -> handlePurchase(purchase)
                                Purchase.PurchaseState.PURCHASED -> if (purchase.isAcknowledged) {
//                                if (purchase.isAutoRenewing) {
                                    purchaseTime = purchase.purchaseTime
                                    purchased = true
                                    temPurchase = purchase
//                                }
                                } else {
                                    handlePurchase(purchase)
                                }
                                else -> {
                                    handlePurchase(purchase)
                                }
                            }

                        }

                        if (purchased) {
//                            BusUtils.post(BUS_TAG_BUY_STATE_PURCHASED, temPurchase!!)
                            BusUtils.post(MainnnActivity.BUS_TAG_BUY_STATE_PURCHASED, temPurchase!!)
                        }
                    } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                        //用户取消购买
                        //处理由用户取消采购流程引起的错误。
                        ToastUtils.showShort("Transaction cancelled")
                    } else {
                        //处理任何其他错误代码。
                            if (billingResult.debugMessage.isNotBlank()) {
                                ToastUtils.showShort(billingResult.debugMessage)
                            }

                    }
                }

        //连接监听
        billingClientStateListener = object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                context.loadingDialog.dismiss()
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    connectionNum = 0
                    handler.sendEmptyMessage(1)

                    if (conListener != null) {
                        conListener.connectSuc()
                    }
                } else {
                    // 需要实现重连机制?
                    reConnect()
                }
            }

            override fun onBillingServiceDisconnected() {
                // 连接断开，实现重连机制
                reConnect()
            }
        }

        //处理操作监听
        acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
            if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.e("====>", "buy success")

                //处理消费操作的成功。
                if (temPurchase != null) {
                    BusUtils.post(MainnnActivity.BUS_TAG_BUY_STATE_PURCHASED, temPurchase!!)
                }
                ToastUtils.showShort("Success")
            } else {
                ToastUtils.showShort(it.debugMessage)
            }
        }

//        setOnClickListener()
    }

    /*** 重连 */
    private fun reConnect() {
        if (!context.isFinishing) {
            when (connectionNum) {
                0, 1 -> {
                    handler.sendEmptyMessageDelayed(0, 1000 * 5)
                }
                2 -> {
                    handler.sendEmptyMessageDelayed(0, 1000 * 60)
                }
                3 -> {
                    handler.sendEmptyMessageDelayed(0, 1000 * 60 * 5)
                }
            }
        }
    }

    /*** 购买 */
    fun buy(vipId: String) {

       selectVipID = vipId
        if (connectionNum == 0) {
            subscribe()
        } else {
            ToastUtils.showShort("Google Play not connected.")
            clientConnection()
        }
    }

    /*** 初始化 */
    private fun init() {
//        BusUtils.register(this)
        //初始化 BillingClient
        billingClient = BillingClient.newBuilder(context)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build()

        clientConnection()

    }

    /*** 与 Google Play 建立连接 */
    private fun clientConnection() {
        if (!context.isFinishing) {
            //与 Google Play 建立连接
//            context.showProgressDialog()
            connectionNum++
            billingClient.startConnection(billingClientStateListener)
        }
    }

    /*** 订阅 */
    private fun subscribe() {
        if (skuDetailsList.isNullOrEmpty()) {
            querySku(true)
        } else {

            var vipID = selectVipID

            var skuDetails: SkuDetails? = null
            for (temp in skuDetailsList) {
                if (temp.sku == vipID) {
                    skuDetails = temp
                    break
                }
            }

            if (skuDetails == null) {
                ToastUtils.showShort("no options-$vipID")
            } else {
                val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails!!)
                        .build()
                val responseCode =
                        billingClient.launchBillingFlow(context, flowParams).responseCode
            }
        }
    }

    /*** 查询SKU */
    private fun querySku(doSub: Boolean) {
        var result = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS)
        //查询可供购买的商品
        //通过调用querySkuDetailsAsync（）检索“skuDetails”的值。
        val skuList: MutableList<String> = ArrayList()
        skuList.add(Constant.VIP_MONTH)
//        skuList.add(AppConfig.PURCHASE_HALF_YEAR_ID)
//        skuList.add(AppConfig.PURCHASE_YEAR_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)
        billingClient.querySkuDetailsAsync(params.build(),
                SkuDetailsResponseListener { billingResult, skuDetailsList ->
                    if (skuDetailsList.isNullOrEmpty()) {
                        ToastUtils.showShort("no options")
                    } else {
                        this.skuDetailsList.clear()
                        this.skuDetailsList.addAll(skuDetailsList)

                        if (doSub) {
                            subscribe()
                        }
                    }
                })
    }

    /*** 查询购买交易 */
    fun queryPurchases() {
        //查询购买交易
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS) { billingResult, purchases ->
            if (MainnnActivity.purchased) {
                return@queryPurchasesAsync
            }
            //购买结果监听
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                MainnnActivity.purchased = false
                MainnnActivity.purchaseTime = 0
                MainnnActivity.productId =""
                if (purchases.isNullOrEmpty()) {
                    return@queryPurchasesAsync
                } else {
                    for (purchase in purchases) {
                        //验证购买情况。//确保尚未为此purchaseToken授予权限。//授予用户权限。
                        when (purchase.purchaseState) {
                            Purchase.PurchaseState.PENDING -> handlePurchase(purchase)
                            Purchase.PurchaseState.PURCHASED -> if (purchase.isAcknowledged) {
                                temPurchase = purchase
                                BusUtils.post(MainnnActivity.BUS_TAG_BUY_STATE_PURCHASED, temPurchase!!)
                            } else {
                                handlePurchase(purchase)
                            }
                            else -> {
                                handlePurchase(purchase)
                            }
                        }
                    }
                }

                handler.sendEmptyMessage(2)

            }

        }
    }

    //处理购买交易
    private fun handlePurchase(purchase: Purchase) {
        temPurchase = purchase
        purchaseTime = purchase.purchaseTime

        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

//        context.showProgressDialog()
        billingClient.acknowledgePurchase(
                acknowledgePurchaseParams,
                acknowledgePurchaseResponseListener
        )

    }

    fun destory() {
        billingClient?.endConnection()
    }

}

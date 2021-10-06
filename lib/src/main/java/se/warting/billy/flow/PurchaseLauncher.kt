package se.warting.billy.flow

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult

interface PurchaseLauncher {
    fun buy(billingFlowParams: BillingFlowParams): BillingResult?
}

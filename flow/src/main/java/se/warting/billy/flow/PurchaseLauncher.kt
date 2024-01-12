package se.warting.billy.flow

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult

public interface PurchaseLauncher {
    public fun buy(billingFlowParams: BillingFlowParams): BillingResult?
}

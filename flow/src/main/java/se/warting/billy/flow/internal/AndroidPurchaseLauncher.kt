package se.warting.billy.flow.internal

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import se.warting.billy.flow.PurchaseLauncher

internal class AndroidPurchaseLauncher(
    private val billingClient: BillingClient,
    private val provider: AndroidActivityProvider
) : PurchaseLauncher {

    override fun buy(billingFlowParams: BillingFlowParams): BillingResult? {
        var result: BillingResult? = null
        provider.get()?.let {
            result = billingClient.launchBillingFlow(it, billingFlowParams)
        }
        return result
    }
}

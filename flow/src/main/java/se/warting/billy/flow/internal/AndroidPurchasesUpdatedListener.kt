package se.warting.billy.flow.internal

import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.AcknowledgePurchaseResponseListener
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import se.warting.billy.flow.BillingProvider

internal class AndroidPurchasesUpdatedListener : PurchasesUpdatedListener {

    private val acknowledgePurchaseResponseListener = AcknowledgePurchaseResponseListener {
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                BillingProvider.instance.billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams.build(),
                    acknowledgePurchaseResponseListener
                )
            }
        }
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                acknowledgePurchase(purchase)
            }
        }
//        else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//            // Handle an error caused by a user cancelling the purchase flow.
//        }
//        else {
//            // Handle any other error codes.
//        }
    }
}

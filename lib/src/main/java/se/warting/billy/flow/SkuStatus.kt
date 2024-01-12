package se.warting.billy.flow

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails

/**
 * Defines the status of a given [Sku]
 */
public sealed class SkuStatus {

    /**
     * The given [Sku] from this status
     */
    public abstract val type: Sku

    public data class Owned(override val type: Sku, val purchase: List<Purchase>) : SkuStatus()

    public data class Loading(override val type: Sku) : SkuStatus()

    public data class Available(override val type: Sku, val skuDetails: SkuDetails) : SkuStatus() {
        public fun buy() {
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build()
            buy(flowParams)
        }

        // maybe public?
        private fun buy(billingFlowParams: BillingFlowParams) {
            BillingProvider.instance.buy(billingFlowParams)
        }
    }

    public data class Unavailable(override val type: Sku) : SkuStatus()
}

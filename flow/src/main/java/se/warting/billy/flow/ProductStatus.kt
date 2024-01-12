package se.warting.billy.flow

import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase

/**
 * Defines the status of a given [Product]
 */
public sealed class ProductStatus {

    /**
     * The given [Product] from this status
     */
    public abstract val type: Product

    public data class Owned(override val type: Product, val purchase: List<Purchase>) : ProductStatus()

    public data class Loading(override val type: Product) : ProductStatus()

    public data class Available(
        override val type: Product,
        val productDetails: ProductDetails
    ) : ProductStatus() {
        public fun buy(offer: ProductDetails.SubscriptionOfferDetails) {

            val productDetailsParams: BillingFlowParams.ProductDetailsParams =
                BillingFlowParams.ProductDetailsParams.newBuilder()
                    .setOfferToken(offer.offerToken)
                    .setProductDetails(productDetails)
                    .build()

            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productDetailsParams))
                .build()
            buy(flowParams)
        }

        // maybe public?
        private fun buy(billingFlowParams: BillingFlowParams) {
            BillingProvider.instance.buy(billingFlowParams)
        }
    }

    public data class Unavailable(override val type: Product) : ProductStatus()
}

package se.warting.billy.flow

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.queryProductDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public sealed class Product {

    public abstract val name: String
    public abstract val productType: String

    public class Subscription(override val name: String) : Product() {
        override val productType: String
            get() = BillingClient.ProductType.SUBS
    }

    public class InAppProduct(override val name: String) : Product() {
        override val productType: String
            get() = BillingClient.ProductType.INAPP
    }

    public val statusFlow: Flow<ProductStatus>
        get() = BillingProvider.instance.getStatusFlow(this)

    public val detailsFlow: Flow<List<ProductDetails>>
        get() = flow {
            val products = ArrayList<String>()
            products.add(name)

            val productList = ArrayList<QueryProductDetailsParams.Product>()
            productList.add(productOf(name, productType))

            val productDetailsParams = QueryProductDetailsParams
                .newBuilder()
                .setProductList(productList)
                .build()

            val productDetails: List<ProductDetails> =
                BillingProvider.instance.billingClient.queryProductDetails(productDetailsParams).productDetailsList
                    ?: listOf()

            emit(productDetails)
        }

    private fun productOf(name: String, productType: String): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(name)
            .setProductType(productType)
            .build()
    }
}

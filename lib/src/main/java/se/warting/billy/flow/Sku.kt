package se.warting.billy.flow

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import com.android.billingclient.api.querySkuDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public sealed class Sku {

    public abstract val name: String
    public abstract val skuType: String

    public class Subscription(override val name: String) : Sku() {
        override val skuType: String
            get() = BillingClient.SkuType.SUBS
    }

    public class InAppProduct(override val name: String) : Sku() {
        override val skuType: String
            get() = BillingClient.SkuType.INAPP
    }

    public val statusFlow: Flow<SkuStatus>
        get() = BillingProvider.instance.getStatusFlow(this)

    public val detailsFlow: Flow<List<SkuDetails>>
        get() = flow {
            val skuList = ArrayList<String>()
            skuList.add(name)

            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(skuType)

            val r =
                BillingProvider.instance.billingClient.querySkuDetails(params.build()).skuDetailsList
                    ?: listOf()
            emit(r)
        }
}

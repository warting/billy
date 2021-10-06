package se.warting.billy.flow.internal

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchaseHistoryRecord
import com.android.billingclient.api.PurchaseHistoryResponseListener
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.SkuDetails
import com.android.billingclient.api.SkuDetailsParams
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import se.warting.billy.flow.PurchaseObserver
import se.warting.billy.flow.Sku
import se.warting.billy.flow.SkuStatus

internal data class CombinedPurchaseData(
    val itemPurchases: List<Purchase>,
    val subPurchases: List<Purchase>
) {
    val purchases: List<Purchase>
        get() = itemPurchases + subPurchases
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class AndroidPurchasesObserver(
    private val billingClient: BillingClient,
) : PurchaseObserver, LifecycleEventObserver {

    private val _subPurchasesHistoryStateFlow: MutableStateFlow<List<PurchaseHistoryRecord>> by lazy {
        MutableStateFlow(listOf())
    }
    private val _itemPurchasesHistoryStateFlow: MutableStateFlow<List<PurchaseHistoryRecord>> by lazy {
        MutableStateFlow(listOf())
    }

    private val _subPurchasesStateFlow: MutableStateFlow<List<Purchase>> by lazy {
        MutableStateFlow(listOf())
    }
    private val _itemPurchasesStateFlow: MutableStateFlow<List<Purchase>> by lazy {
        MutableStateFlow(listOf())
    }

    override fun getActiveSubscriptions(): Flow<List<Purchase>> {
        return _subPurchasesStateFlow
    }

    private fun getCombinedPurchasesFlow() =
        combine(_subPurchasesStateFlow, _itemPurchasesStateFlow) { subPurchases, itemPurchases ->
            CombinedPurchaseData(
                subPurchases = subPurchases,
                itemPurchases = itemPurchases
            )
        }

    override fun getStatusFlow(sku: Sku): Flow<SkuStatus> {
        return combine(getCombinedPurchasesFlow(), sku.detailsFlow) { alles, skuDetailsList ->
            val skuList = ArrayList<String>()
            skuList.add(sku.name)
            val params = SkuDetailsParams.newBuilder()
            params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS)

            skuStatusResolver(
                sku,
                skuDetailsList,
                alles.purchases
            )
        }
    }

    private fun skuStatusResolver(
        sku: Sku,
        skuDetailsList: List<SkuDetails>,
        ownedItemsList: List<Purchase>,
    ): SkuStatus {

        val ownedItem = ownedItemsList.filter { it.skus.contains(sku.name) }
        if (ownedItem.isNotEmpty()) {
            return SkuStatus.Owned(sku, ownedItem)
        }

        val skuDetails: SkuDetails? = skuDetailsList.firstOrNull { it.sku == sku.name }
        return if (skuDetails != null) {
            SkuStatus.Available(sku, skuDetails)
        } else {
            SkuStatus.Unavailable(sku)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            ON_CREATE -> refreshStatus()
            ON_RESUME -> refreshStatus()
            else -> return
        }
    }

    private val subHistoryObserver =
        PurchaseHistoryResponseListener { billingResult, purchaseHistoryRecordList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _subPurchasesHistoryStateFlow.value = purchaseHistoryRecordList ?: listOf()
            }
        }

    private val inappHistoryObserver =
        PurchaseHistoryResponseListener { billingResult, purchaseHistoryRecordList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _itemPurchasesHistoryStateFlow.value = purchaseHistoryRecordList ?: listOf()
            }
        }

    private val subsPurchasesObserver = PurchasesResponseListener { billingResult, purchasesList ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _subPurchasesStateFlow.value = purchasesList
        }
    }

    private val inAppPurchasesObserver = PurchasesResponseListener { billingResult, purchasesList ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            _itemPurchasesStateFlow.value = purchasesList.map {
                it
            }
        }
    }

    override fun refreshStatus() {
        if (isConnected) {
            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, subHistoryObserver)
            billingClient.queryPurchaseHistoryAsync(
                BillingClient.SkuType.INAPP,
                inappHistoryObserver
            )
            billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, subsPurchasesObserver)
            billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, inAppPurchasesObserver)
        }
    }

    private var isConnected = false
    override fun connected(connected: Boolean) {
        isConnected = true
    }
}

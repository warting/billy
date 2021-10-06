package se.warting.billy.flow

import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow

/**
 * Observes purchase status changes
 */
interface PurchaseObserver {

    /**
     * @return a flow with all purchases made
     */
    fun getActiveSubscriptions(): Flow<List<Purchase>>

    fun getStatusFlow(sku: Sku): Flow<SkuStatus>

    /**
     * Request the observer to refresh the status of the purchases
     *
     * Note: this is called on lifecycle changes
     */
    fun refreshStatus()
    fun connected(connected: Boolean)
}

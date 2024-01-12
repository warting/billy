package se.warting.billy.flow

import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow

/**
 * Observes purchase status changes
 */
public interface PurchaseObserver {

    /**
     * @return a flow with all purchases made
     */
    public fun getActiveSubscriptions(): Flow<List<Purchase>>

    public fun getStatusFlow(sku: Sku): Flow<SkuStatus>

    /**
     * Request the observer to refresh the status of the purchases
     *
     * Note: this is called on lifecycle changes
     */
    public fun refreshStatus()
    public fun connected(connected: Boolean)
}

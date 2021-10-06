package se.warting.billy.flow

import android.content.Context
import androidx.annotation.Keep
import androidx.startup.Initializer

/**
 * Automatically wire the Billing checker and observer with the application lifecycle
 *
 * Do not call BillingInitializer.create directly unless it's required for testing or
 * some custom initialization.
 */
@Keep
class BillingInitializer : Initializer<BillingProvider> {

    override fun create(context: Context): BillingProvider {
        return BillingProvider.run {
            init(context)
            instance
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}

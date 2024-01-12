package se.warting.billy.flow.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference

internal class AndroidActivityProvider : Application.ActivityLifecycleCallbacks {

    private var currentActivity: WeakReference<Activity>? = null

    fun get(): Activity? = currentActivity?.get()

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // On pre-created is only called for 29+, just check if the activity was not already there
        if (activity != currentActivity?.get()) {
            currentActivity = WeakReference(activity)
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity == currentActivity?.get()) {
            currentActivity?.clear()
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity != currentActivity?.get()) {
            currentActivity = WeakReference(activity)
        }
    }

    @SuppressWarnings("EmptyFunctionBlock")
    override fun onActivityPaused(activity: Activity) {
    }

    @SuppressWarnings("EmptyFunctionBlock")
    override fun onActivityStarted(activity: Activity) {
    }

    @SuppressWarnings("EmptyFunctionBlock")
    override fun onActivityStopped(activity: Activity) {
    }

    @SuppressWarnings("EmptyFunctionBlock")
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }
}

package se.warting.sampleapp.advance

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.warting.billy.flow.SkuStatus
import se.warting.sampleapp.R

class AdvanceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.advance_activity)

        val locationText = findViewById<TextView>(R.id.locationText)
        val actionButton = findViewById<FloatingActionButton>(R.id.locationFab)

        var buyableProduct: SkuStatus.Available? = null

        actionButton.setOnClickListener {
            buyableProduct?.buy()
        }

        // Update view based on ViewModel output
        viewModel.getViewData().observe(this) { viewData ->

            buyableProduct = when (val s = viewData.earlyBirdStatus) {
                is SkuStatus.Available -> s
                is SkuStatus.Loading -> null
                is SkuStatus.Owned -> null
                is SkuStatus.Unavailable -> null
            }
            locationText.text =
                getString(
                    R.string.products, when (viewData.earlyBirdStatus) {
                        is SkuStatus.Available -> "Available"
                        is SkuStatus.Loading -> "Loading"
                        is SkuStatus.Owned -> "Owned"
                        is SkuStatus.Unavailable -> "Unavailable"
                    }
                )
        }
    }

    private val viewModel: AdvanceViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AdvanceViewModel() as T
            }
        }
    }
}

package se.warting.sampleapp.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import se.warting.billy.flow.Sku
import se.warting.billy.flow.SkuStatus

@Composable
fun ComposeBillingScreen() {

    val earlyBirdProduct = Sku.Subscription("early_bird")

    val earlyBirdProductStatus by earlyBirdProduct.statusFlow.collectAsState(
        initial = SkuStatus.Loading(earlyBirdProduct)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(earlyBirdProduct.name)

        when (@Suppress("UnnecessaryVariable") val status = earlyBirdProductStatus) {
            is SkuStatus.Available -> {
                Text("Available!")
                Button(onClick = {
                    status.buy()
                }) {
                    Text(text = "buy")
                }
            }
            is SkuStatus.Loading -> Text("Loading....")
            is SkuStatus.Unavailable -> Text("Unavailable")
            is SkuStatus.Owned -> Text("Owned")
        }
    }
}

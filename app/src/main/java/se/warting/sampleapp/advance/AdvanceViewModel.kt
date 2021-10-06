package se.warting.sampleapp.advance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import se.warting.billy.flow.Sku
import se.warting.billy.flow.SkuStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AdvanceViewModel : ViewModel() {

    private val viewData = MutableLiveData<AdvanceViewData>()

    init {
        Sku.Subscription("early_bird").statusFlow.onEach {
            viewData.value =
                viewData.value?.copy(
                    earlyBirdStatus = it,
                ) ?: AdvanceViewData(
                    earlyBirdStatus = it,
                )
        }.launchIn(viewModelScope)
    }

    fun getViewData(): LiveData<AdvanceViewData> = viewData
}

data class AdvanceViewData(
    val earlyBirdStatus: SkuStatus = SkuStatus.Loading(Sku.Subscription("early_bird")),
)

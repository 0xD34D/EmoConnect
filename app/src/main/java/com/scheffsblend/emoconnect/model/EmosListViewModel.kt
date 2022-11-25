package com.scheffsblend.emoconnect.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.clj.fastble.data.BleDevice
import com.scheffsblend.emoconnect.data.DataSource

class EmosListViewModel(val dataSource: DataSource) : ViewModel() {

    val emosLiveData = dataSource.getEmosList()

    /* Add BleDevice (Emo) to the datasource */
    fun insertEmo(emo: BleDevice?) {
        if (emo == null) {
            return
        }
        dataSource.addEmo(emo)
    }

    // Define ViewModel factory in a companion object
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                EmosListViewModel(
                    dataSource = DataSource.getDataSource()
                )
            }
        }
    }
}
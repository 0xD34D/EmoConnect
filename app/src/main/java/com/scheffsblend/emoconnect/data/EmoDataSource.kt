package com.scheffsblend.emoconnect.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.clj.fastble.data.BleDevice

/* Handles operations on emosLiveData and holds details about it. */
class DataSource() {
    private val initialEmosList: List<BleDevice> = ArrayList()
    private val emosLiveData = MutableLiveData(initialEmosList)

    /* Adds emo to liveData and posts value. */
    fun addEmo(emo: BleDevice) {
        val currentList = emosLiveData.value
        if (currentList == null) {
            emosLiveData.postValue(listOf(emo))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, emo)
            emosLiveData.postValue(updatedList)
        }
    }

    /* Removes emo from liveData and posts value. */
    fun removeEmo(emo: BleDevice) {
        val currentList = emosLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(emo)
            emosLiveData.postValue(updatedList)
        }
    }

    /* Returns emo given a name. */
    fun getEmoForName(name: String): BleDevice? {
        emosLiveData.value?.let { emos ->
            return emos.firstOrNull{ it.name == name}
        }
        return null
    }

    /* Returns emo given a MAC address. */
    fun getEmoForMacAddress(macAddr: String): BleDevice? {
        emosLiveData.value?.let { emos ->
            return emos.firstOrNull{ it.mac == macAddr}
        }
        return null
    }

    fun getEmosList(): LiveData<List<BleDevice>> {
        return emosLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
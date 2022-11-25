package com.scheffsblend.emoconnect

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.scan.BleScanRuleConfig
import com.scheffsblend.emoconnect.databinding.FragmentScanBinding
import com.scheffsblend.emoconnect.model.Constants
import com.scheffsblend.emoconnect.model.EmosAdapter
import com.scheffsblend.emoconnect.model.EmosListViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val bleManager get() = BleManager.getInstance()
    private val emosListViewModel by viewModels<EmosListViewModel> {EmosListViewModel.Factory}
    private val TAG = "FirstFragment"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emosAdapter = EmosAdapter { emo -> (emo)}
        binding.emosAvailable.adapter = emosAdapter
        if (this.context != null) {
            emosListViewModel.emosLiveData.observe(viewLifecycleOwner) {
                it?.let {
                    emosAdapter.submitList(it as MutableList<BleDevice>)
                }
            }
        }

        binding.buttonScan.setOnClickListener {
            binding.buttonScan.isEnabled = false
            emosAdapter.submitList(null)
            searchEmo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchEmo() {
        if (bleManager.isBlueEnable) {
            val bleScanRuleConfig = BleScanRuleConfig.Builder()
                .setDeviceName(true, Constants.bleName)
                .setScanTimeOut(5000L)
                .build()
            bleManager.initScanRule(bleScanRuleConfig)
            bleManager.scan(object: BleScanCallback() {
                override fun onScanStarted(success: Boolean) {
                    Log.d(TAG, "onScanStarted(${success})")
                }

                @SuppressLint("MissingPermission")
                override fun onScanning(bleDevice: BleDevice?) {
                    Log.d(TAG, "onScanning(${bleDevice!!.device.name})")
                    emosListViewModel.insertEmo(bleDevice)
                }

                override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                    binding.buttonScan.isEnabled = true
                }
            })
        }
    }
}
package com.example.printermobile.ui.Views.advance


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printermobile.R
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.databinding.FragmentPrinterTypeBinding
import com.example.printermobile.domain.models.BluetoothDomain
import com.example.printermobile.ui.ViewModels.BluetoothViewModel
import com.example.printermobile.ui.adapters.bluetooth.BluetoothAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PrinterTypeFragment : Fragment() {

    private var _binding: FragmentPrinterTypeBinding? = null
    private val advancePrinterViewModel by activityViewModels<AdvancePrinterViewModel>()
    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private var pairedDeviceList:List<BluetoothDomain> = listOf()
    private var scannedDeviceList:List<BluetoothDomain> = listOf()
    private var pairedDeviceAdapter = BluetoothAdapter(pairedDeviceList)
    private var scannedDeviceAdapter = BluetoothAdapter(scannedDeviceList)
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrinterTypeBinding.inflate(layoutInflater, container, false)
        initUI()
        initData()
        initListeners()
        lifecycleScope.launch {
            bluetoothViewModel.state.collect {
                scannedDeviceList = it.scannedDevices
                pairedDeviceList = it.pairedDevices
                pairedDeviceAdapter.updateList(pairedDeviceList)
                scannedDeviceAdapter.updateList(scannedDeviceList)
            }
        }

        return binding.root
    }

    private fun initData() {
        binding.etPort.setText(advancePrinterViewModel.port.value)
        binding.etIPAddress.setText(advancePrinterViewModel.ipAddress.value)
        binding.etName.setText(advancePrinterViewModel.name.value)
    }

    private fun initUI() {
        if (advancePrinterViewModel.printerType.value.isNullOrEmpty()) {
            setCardsSelectedState(PrinterType.WIFI.type)
        } else {
            setCardsSelectedState(advancePrinterViewModel.printerType.value!!)
        }
        binding.rvPairedDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPairedDevice.adapter = pairedDeviceAdapter
        binding.rvScannedDevice.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScannedDevice.adapter = scannedDeviceAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListeners() {
        binding.ibDialogClose.setOnClickListener {
            val slideDown = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_up)
            binding.clBluetoothDeviceDialog.animation = slideDown
            binding.clBluetoothDeviceDialog.visibility = View.INVISIBLE
        }
        binding.clBluetoothDeviceDialog.setOnClickListener {

        }
        binding.cvWifi.setOnClickListener {
            setCardsSelectedState(PrinterType.WIFI.type)
        }
        binding.cvBluetooth.setOnClickListener {
            setCardsSelectedState(PrinterType.BLUETOOTH.type)
            try {
                bluetoothViewModel.startScan()
                showDialog()
            } catch (e: Exception) {
                println(e)
            }
        }
        binding.cvUSB.setOnClickListener {
            setCardsSelectedState(PrinterType.USB.type)
        }
        binding.btnPrintTypeNext.setOnClickListener {
            if (checkForm()) {
                if (advancePrinterViewModel.progression.value!! <= 1) {
                    advancePrinterViewModel.progression.value = 1
                }
                advancePrinterViewModel.port.value = binding.etPort.text.toString()
                advancePrinterViewModel.ipAddress.value = binding.etIPAddress.text.toString()
                advancePrinterViewModel.name.value = binding.etName.text.toString()
                advancePrinterViewModel.printTest(requireActivity().applicationContext)
                findNavController().navigate(R.id.action_printerTypeFragment_to_printerConfigFragment)
            }
        }
    }

    private fun showDialog() {
        val slideUp = AnimationUtils.loadAnimation(requireContext(),R.anim.slide_down)
        binding.clBluetoothDeviceDialog.animation = slideUp
        binding.clBluetoothDeviceDialog.visibility = View.VISIBLE
    }
    private fun inputVisibilityChange(param: Boolean) {
        if (param) {
            val fadeIn =
                AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.fade_in)
            binding.etIPAddress.visibility = View.VISIBLE
            binding.etPort.visibility = View.VISIBLE
            binding.tilIpAddress.visibility = View.VISIBLE
            binding.tilPort.visibility = View.VISIBLE
            binding.etIPAddress.animation = fadeIn
            binding.etPort.animation = fadeIn
            binding.tilIpAddress.animation = fadeIn
            binding.tilPort.animation = fadeIn
        } else {
            val fadeOut =
                AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.fade_out)
            binding.tilPort.animation = fadeOut
            binding.tilIpAddress.animation = fadeOut
            binding.etIPAddress.animation = fadeOut
            binding.etPort.animation = fadeOut
            binding.tilPort.visibility = View.INVISIBLE
            binding.tilIpAddress.visibility = View.INVISIBLE
            binding.etPort.visibility = View.INVISIBLE
            binding.etIPAddress.visibility = View.INVISIBLE
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (binding.etName.text.isBlank()) {
            binding.etName.error = "El nombre es requerido"
            error = false
        }
        if (binding.etPort.text.isBlank() && advancePrinterViewModel.printerType.value == PrinterType.WIFI.type) {
            binding.etPort.error = "El puerto es requerido"
            error = false
        }
        if (binding.etIPAddress.text.isBlank() && advancePrinterViewModel.printerType.value == PrinterType.WIFI.type) {
            binding.etIPAddress.error = "La dirección es requerida"
            error = false
        }
        return error
    }

    private fun setCardsSelectedState(params: String) {
        when (params) {
            PrinterType.WIFI.type -> {
                advancePrinterViewModel.printerType.value = PrinterType.WIFI.type
                binding.cvWifi.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )

                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                inputVisibilityChange(true)
            }

            PrinterType.BLUETOOTH.type -> {
                advancePrinterViewModel.printerType.value = PrinterType.BLUETOOTH.type
                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )


                binding.cvWifi.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )

                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                inputVisibilityChange(false)
            }

            else -> {
                advancePrinterViewModel.printerType.value = PrinterType.USB.type
                binding.cvUSB.setCardBackgroundColor(resources.getColor(R.color.primary))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.white
                    )
                )


                binding.cvBluetooth.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivBluetooth.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.cvWifi.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        R.color.textPrimary
                    )
                )
                inputVisibilityChange(false)
            }
        }
    }
}
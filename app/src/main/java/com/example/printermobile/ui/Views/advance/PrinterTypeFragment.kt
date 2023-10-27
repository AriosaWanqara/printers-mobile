package com.example.printermobile.ui.Views.advance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.printermobile.R
import com.example.printermobile.core.printType.PrinterType
import com.example.printermobile.databinding.FragmentPrinterTypeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrinterTypeFragment : Fragment() {

    private var _binding: FragmentPrinterTypeBinding? = null
    private val advancePrinterViewModel by activityViewModels<AdvancePrinterViewModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrinterTypeBinding.inflate(layoutInflater, container, false)

        initUI()
        initListeners()
        return binding.root
    }

    private fun initUI() {
        if (advancePrinterViewModel.printerType.value.isNullOrEmpty()){
            setCardsSelectedState(PrinterType.WIFI.type)
        }else{
            setCardsSelectedState(advancePrinterViewModel.printerType.value!!)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initListeners() {
        binding.cvWifi.setOnClickListener {
            setCardsSelectedState(PrinterType.WIFI.type)
        }
        binding.cvBluetooth.setOnClickListener {
            setCardsSelectedState(PrinterType.BLUETOOTH.type)
        }
        binding.cvUSB.setOnClickListener {
            setCardsSelectedState(PrinterType.USB.type)
        }
        binding.btnPrintTypeNext.setOnClickListener {
            if (checkForm()) {
                advancePrinterViewModel.port.value = binding.etPort.text.toString()
                advancePrinterViewModel.ipAddress.value = binding.etIPAddress.text.toString()
                advancePrinterViewModel.name.value = binding.etName.text.toString()
                advancePrinterViewModel.printTest(requireActivity().applicationContext)
                findNavController().navigate(R.id.action_printerTypeFragment_to_printerConfigFragment)
            }
        }
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
            binding.tilPort.visibility = View.GONE
            binding.tilIpAddress.visibility = View.GONE
            binding.etPort.visibility = View.GONE
            binding.etIPAddress.visibility = View.GONE
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (binding.etName.text.isBlank()){
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
                        android.R.color.darker_gray
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
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
                        android.R.color.darker_gray
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )

                binding.cvUSB.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivUSB.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                binding.tvUSB.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
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
                        android.R.color.darker_gray
                    )
                )
                binding.tvBluetooth.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                binding.cvWifi.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
                binding.ivWifi.setColorFilter(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                binding.tvWifi.setTextColor(
                    ContextCompat.getColor(
                        requireActivity().applicationContext,
                        android.R.color.darker_gray
                    )
                )
                inputVisibilityChange(false)
            }
        }
    }
}
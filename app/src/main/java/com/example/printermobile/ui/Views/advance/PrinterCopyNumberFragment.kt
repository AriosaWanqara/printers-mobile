package com.example.printermobile.ui.Views.advance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.printermobile.R
import com.example.printermobile.databinding.FragmentPrinterCopyNumberBinding

class PrinterCopyNumberFragment : Fragment() {

    private var _binding: FragmentPrinterCopyNumberBinding? = null
    private val binding get() = _binding!!
    private val advancePrinterViewModel by activityViewModels<AdvancePrinterViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrinterCopyNumberBinding.inflate(layoutInflater, container, false)

        initData()
        initListeners()
        return binding.root
    }

    private fun initData() {
        binding.etCopies.setText(advancePrinterViewModel.copyNumber.value)
    }

    private fun initListeners() {
        binding.btnPrintConfigBack.setOnClickListener {
            findNavController().navigate(R.id.action_printerCopyNumberFragment_to_printerConfigFragment)
        }
        binding.btnPrintConfigNext.setOnClickListener {
            if (checkForm()) {
                if (advancePrinterViewModel.progression.value!! <= 3) {
                    advancePrinterViewModel.progression.value = 3
                }
                advancePrinterViewModel.copyNumber.value = binding.etCopies.text.toString()
                findNavController().navigate(R.id.action_printerCopyNumberFragment_to_printerDocumentTypeFragment)
            }
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (binding.etCopies.text.isBlank()) {
            binding.etCopies.error = "El numero de copias es requerido"
            error = false
        }
        return error
    }

}
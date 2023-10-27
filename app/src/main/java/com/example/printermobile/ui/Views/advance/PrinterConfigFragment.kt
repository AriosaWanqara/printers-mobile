package com.example.printermobile.ui.Views.advance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.printermobile.R
import com.example.printermobile.databinding.FragmentPrinterConfigBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.example.printermobile.core.printType.PrinterType

@AndroidEntryPoint
class PrinterConfigFragment : Fragment() {
    private var _binding: FragmentPrinterConfigBinding? = null
    private val advancePrinterViewModel by activityViewModels<AdvancePrinterViewModel>()
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrinterConfigBinding.inflate(layoutInflater, container, false)
        initListeners()
        return binding.root
    }

    private fun initListeners() {
        binding.btnPrintConfigBack.setOnClickListener {
            findNavController().navigate(R.id.action_printerConfigFragment_to_printerTypeFragment)
        }
        binding.btnPrintConfigNext.setOnClickListener {
            if (checkForm()) {
                advancePrinterViewModel.charactersNumber.value = binding.etCharacters.text.toString()
                findNavController().navigate(R.id.action_printerConfigFragment_to_printerCopyNumberFragment)
            }
        }
        binding.btnPrintTest.setOnClickListener {
            advancePrinterViewModel.printTest(requireActivity().applicationContext)
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (binding.etCharacters.text.isBlank()) {
            binding.etCharacters.error = "El numero de caracteres es requerido"
            error = false
        }
        return error
    }
}
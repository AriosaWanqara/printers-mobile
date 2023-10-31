package com.example.printermobile.ui.Views.advance

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.databinding.FragmentPrinterDocumentTypeBinding
import com.example.printermobile.ui.Views.ListPrintersActivity
import com.example.printermobile.ui.Views.Printer.MissingPrinterAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrinterDocumentTypeFragment : Fragment() {

    private val advancePrinterViewModel by activityViewModels<AdvancePrinterViewModel>()
    private var _binding: FragmentPrinterDocumentTypeBinding? = null
    private lateinit var adapter: MissingPrinterAdapter
    private val binding get() = _binding!!
    private var documentType: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrinterDocumentTypeBinding.inflate(layoutInflater, container, false)
        initData()
        initUI()
        initListeners()
        return binding.root
    }

    private fun initUI() {
        binding.rvDocumentTypeList.layoutManager =
            GridLayoutManager(
                requireActivity().applicationContext,
                2,
                GridLayoutManager.VERTICAL,
                false
            )

        binding.rvDocumentTypeList.adapter = adapter
    }

    private fun initData() {
        val documentEnum: documentType = documentType()
        documentType = documentEnum.getDocuments()
        adapter = MissingPrinterAdapter(documentType) {
            advancePrinterViewModel.documentType.value =
                if (advancePrinterViewModel.documentType.value!!.contains(it)) {
                    advancePrinterViewModel.documentType.value!!.minus(it)
                } else {
                    advancePrinterViewModel.documentType.value!!.plus(it)
                }
        }
        if (!advancePrinterViewModel.documentType.value.isNullOrEmpty()) {
            adapter.onSelectedItem(advancePrinterViewModel.documentType.value!!)
        }
    }

    private fun initListeners() {
        binding.btnPrintConfigBack.setOnClickListener {
            findNavController().navigate(R.id.action_printerDocumentTypeFragment_to_printerCopyNumberFragment)
        }
        binding.btnPrintConfigNext.setOnClickListener {
            if (checkForm()) {
                if (advancePrinterViewModel.progression.value!! <= 4) {
                    advancePrinterViewModel.progression.value = 4
                }
                CoroutineScope(Dispatchers.IO).launch {
                    advancePrinterViewModel.onAdd()
                    withContext(Dispatchers.Main) {
                        val listPrinterIntent = Intent(
                            requireContext().applicationContext,
                            ListPrintersActivity::class.java
                        )
                        startActivity(listPrinterIntent)
                    }
                }
            }
        }
    }

    private fun checkForm(): Boolean {
        var error = true
        if (advancePrinterViewModel.documentType.value!!.isEmpty()) {
            error = false
            Toast.makeText(
                requireActivity().applicationContext,
                "Debe seleccionar 1 documento",
                Toast.LENGTH_SHORT
            ).show()
        }
        return error
    }

}
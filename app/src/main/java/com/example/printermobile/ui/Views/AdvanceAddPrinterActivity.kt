package com.example.printermobile.ui.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityAdvanceAddPrinterBinding
import com.example.printermobile.ui.Views.advance.AdvancePrinterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvanceAddPrinterActivity : AppCompatActivity() {
    private val advancePrinterViewModel by viewModels<AdvancePrinterViewModel>()
    private lateinit var binding: ActivityAdvanceAddPrinterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvanceAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        advancePrinterViewModel.progression.observe(this, Observer {
            if (it > 0) {
                val x = (it * 100) / 4
                binding.lpiWizardState.setProgress(x,true)
            }
        })
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            val intent = Intent(this, ListPrintersActivity::class.java)
            startActivity(intent)
        }
    }
}
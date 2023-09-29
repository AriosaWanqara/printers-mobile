package com.example.printermobile.ui.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityAddPrinterBinding

class AddPrinterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrinterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        initListeners()
    }

    private fun initUI(){

    }

    private fun initListeners(){

    }
}
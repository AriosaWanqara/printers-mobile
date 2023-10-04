package com.example.printermobile.ui.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.printermobile.R
import com.example.printermobile.core.document.documentType
import com.example.printermobile.databinding.ActivityAddPrinterBinding
import com.example.printermobile.domain.models.Printers

class AddPrinterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPrinterBinding
    private lateinit var name:String
    private lateinit var ipAddress:String
    private lateinit var documentTypeSelected:String
    private var port:Int = 0
    private var characters:Int = 0
    private var copies:Int = 0
    private var isWifi:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
    }

    private fun initUI(){
        val documentType:documentType = documentType()
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            documentType.getDocuments()
        )
        binding.spDocumentType.adapter = adapter
    }

    private fun initListeners(){
    }
}
package com.example.printermobile.ui.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityHelpPrinterBinding
import com.example.printermobile.domain.models.FaQ
import com.example.printermobile.ui.Views.faq.HelpPrinterAdapter

class HelpPrinterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityHelpPrinterBinding
    private var faQList:List<FaQ> = listOf(FaQ(),FaQ())
    private var adapter = HelpPrinterAdapter(faQList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
    }

    private fun initUI() {
        binding.rvFaQList.layoutManager = LinearLayoutManager(this)
        binding.rvFaQList.adapter = adapter
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            val listActivity = Intent(this,ListPrintersActivity::class.java)
            startActivity(listActivity)
        }
    }
}
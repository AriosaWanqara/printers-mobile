package com.example.printermobile.ui.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityHelpPrinterBinding
import com.example.printermobile.domain.models.FaQ
import com.example.printermobile.ui.ViewModels.AddPrinterViewModel
import com.example.printermobile.ui.ViewModels.HelpViewModel
import com.example.printermobile.ui.Views.faq.HelpPrinterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpPrinterActivity : AppCompatActivity() {

    private val helpViewModel: HelpViewModel by viewModels()
    private lateinit var binding: ActivityHelpPrinterBinding
    private var faQList: List<FaQ> = listOf()
    private var adapter = HelpPrinterAdapter(faQList)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()
        helpViewModel.onCreate()
        helpViewModel.faQ.observe(this, Observer {
            faQList = listOf()
            it.map { it ->
                faQList = faQList.plus(it)
            }
            adapter.updateList(faQList)
        })
        helpViewModel.isFaQLoading.observe(this, Observer {
            if (it){
                binding.cpLoading.visibility = View.VISIBLE
            }else{
                binding.cpLoading.visibility = View.GONE
            }
        })
    }

    private fun initUI() {
        binding.rvFaQList.layoutManager = LinearLayoutManager(this)
        binding.rvFaQList.adapter = adapter
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            val listActivity = Intent(this, ListPrintersActivity::class.java)
            startActivity(listActivity)
        }
    }
}
package com.example.printermobile.ui.Views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityHelpPrinterBinding
import com.example.printermobile.domain.models.FaQ
import com.example.printermobile.ui.ViewModels.AddPrinterViewModel
import com.example.printermobile.ui.ViewModels.HelpViewModel
import com.example.printermobile.ui.adapters.faq.HelpPrinterAdapter
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
            if (it) {
                binding.cpLoading.visibility = View.VISIBLE
            } else {
                binding.cpLoading.visibility = View.GONE
            }
        })
    }

    private fun initUI() {
        binding.rvFaQList.layoutManager = LinearLayoutManager(this)
        binding.rvFaQList.adapter = adapter
        binding.topAppBar.menu.removeItem(R.id.miTourPlay)
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            val listActivity = Intent(this, ListPrintersActivity::class.java)
            startActivity(listActivity)
        }
        binding.searchBarr.editText.addTextChangedListener {
            if (binding.searchBarr.editText.text.isNotBlank()) {
                val filterList = faQList.filter { it.getQuest().contains(binding.searchBarr.editText.text) }
                if (filterList.isNotEmpty()){
                    adapter.updateList(filterList)
                }
            }else{
                adapter.updateList(faQList)
            }
        }
    }
}
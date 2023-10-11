package com.example.printermobile.ui.Views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.Observer
import com.example.printermobile.databinding.ActivityListPrintersBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.ListPrintersViewModel
import com.example.printermobile.ui.Views.Printer.ListPrinterAdapter
import com.example.printermobile.ui.Views.Printer.SwipeHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListPrintersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPrintersBinding
    private val listPrintersViewModel: ListPrintersViewModel by viewModels()
    private var printers: List<Printers> = listOf()
    private val adapter = ListPrinterAdapter(printers, onItemRedirect = {
        val id = it.id
        redirect(id!!)
    }, onItemRemove = {
        CoroutineScope(Dispatchers.IO).launch {
            listPrintersViewModel.onDeletePrinter(it.id!!)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPrintersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI()
        initUI()
        initListeners()

        listPrintersViewModel.onCreate()
        listPrintersViewModel.printers.observe(this, Observer {
            printers = listOf()
            it.map { printer ->
                printers = printers.plus(printer)
            }
            adapter.updateList(printers)
        })
    }

    private fun redirect(id: Int) {
        val updatePrinterIntent = Intent(this, UpdatePrinterActivity::class.java)
        updatePrinterIntent.putExtra("printer", id.toString())
        startActivity(updatePrinterIntent)
    }

    private fun initUI() {
        binding.rvPrinterList.layoutManager = LinearLayoutManager(this)
        binding.rvPrinterList.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rvPrinterList) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.rvPrinterList)

    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            this,
            "Delete",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    CoroutineScope(Dispatchers.IO).launch {
                        listPrintersViewModel.onDeletePrinter(printers[position].id!!)
                    }

                }
            })
    }

    private fun initListeners() {
        binding.fabAddPrinter.setOnClickListener {
            val intent = Intent(this, AddPrinterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
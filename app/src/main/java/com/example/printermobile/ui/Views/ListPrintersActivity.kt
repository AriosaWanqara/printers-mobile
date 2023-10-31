package com.example.printermobile.ui.Views

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.core.system.SystemTypeEnum
import com.example.printermobile.databinding.ActivityListPrintersBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.domain.models.SystemType
import com.example.printermobile.ui.ViewModels.ListPrintersViewModel
import com.example.printermobile.ui.Views.Printer.ListPrinterAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ListPrintersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListPrintersBinding
    private val listPrintersViewModel: ListPrintersViewModel by viewModels()
    private var printers: List<Printers> = listOf()
    private var systemType: SystemType? = null;
    private val adapter = ListPrinterAdapter(printers, onItemRedirect = {
        val id = it.id
        redirect(id!!)
    }, onItemRemove = {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Impresora")
        builder.setMessage("¿Esta seguro en eliminar este Resgistro?")
        builder.setPositiveButton(
            "Eliminar"
        ) { _, _ ->
            val printer = it
            CoroutineScope(Dispatchers.IO).launch {
                listPrintersViewModel.onDeletePrinter(it.id!!)
                withContext(Dispatchers.Main) {
                    Snackbar.make(
                        binding.rvPrinterList,
                        "Impresora Eliminada",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Restaurar") {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val entity = printer.createEntityFromPrinterModel()
                                listPrintersViewModel.onAdd(entity)
                            } catch (e: Exception) {
                                println(e)
                            }
                        }
                    }.setActionTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.primary
                        )
                    ).show()
                }
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPrintersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListeners()

        listPrintersViewModel.onCreate()
        listPrintersViewModel.printers.observe(this, Observer {
            printers = it
            adapter.updateList(printers)
            if (adapter.itemCount <= 0 && listPrintersViewModel.isLoading.value == false) {
                val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                binding.clEmptyMessageContainer.apply {
                    visibility = View.VISIBLE
                    animation = fadeIn
                }
            } else {
                binding.clEmptyMessageContainer.apply {
                    visibility = View.INVISIBLE
                }
            }
        })
        resetSystemTypeCards()
        listPrintersViewModel.systemType.observe(this, Observer { system ->
            if (system == null) {
                updateToShop()
            } else {
                systemType = system
                setSystemTypeCard(system.getIsRestaurant())
            }
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

        val simpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val printer = printers[viewHolder.adapterPosition]
                    listPrintersViewModel.onDeletePrinter(printer.id!!)
                    withContext(Dispatchers.Main) {
                        Snackbar.make(
                            binding.rvPrinterList,
                            "Impresora Eliminada",
                            Snackbar.LENGTH_SHORT
                        ).setAction("Restaurar") {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val entity = printer.createEntityFromPrinterModel()
                                    listPrintersViewModel.onAdd(entity)
                                } catch (e: Exception) {
                                    println(e)
                                }
                            }
                        }.setActionTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.primary
                            )
                        ).show()
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.illarli_red
                        )
                    )
                    .addActionIcon(R.drawable.ic_delete)
                    .setActionIconTint(ContextCompat.getColor(applicationContext, R.color.white))
                    .addCornerRadius(2, 8)
                    .setSwipeLeftLabelColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                    .addSwipeLeftLabel("Borrar")
                    .create()
                    .decorate();

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }
        val personalItemTouch = ItemTouchHelper(simpleCallback)
        personalItemTouch.attachToRecyclerView(binding.rvPrinterList)


        binding.cvRestaurant.setOnClickListener {
            if (systemType != null) {
                if (!systemType!!.getIsRestaurant()) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Cambiar tipo de negocio")
                    builder.setMessage("¿Esta seguro que desea cambiar a restaurante?")
                    builder.setPositiveButton(
                        "Cambiar"
                    ) { _, _ ->
                        updateToRestaurant()
                    }
                    builder.setNegativeButton("Cancelar", null)
                    builder.show()
                }
            } else {
                updateToRestaurant()
            }

        }

        binding.cvShop.setOnClickListener {
            if (systemType != null) {
                if (systemType!!.getIsRestaurant()) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Cambiar tipo de negocio")
                    builder.setMessage("¿Esta seguro que desea cambiar a comercios?")
                    builder.setPositiveButton(
                        "Cambiar"
                    ) { _, _ ->
                        updateToShop()
                    }
                    builder.setNegativeButton("Cancelar", null)
                    builder.show()
                }
            } else {
                updateToShop()
            }
        }
    }

    private fun updateToShop() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (systemType == null) {
                    listPrintersViewModel.onAddSystemType(
                        SystemType(
                            1,
                            SystemTypeEnum.COMERCIO.type,
                            false
                        )
                    )
                    withContext(Dispatchers.Main) {
                        setSystemTypeCard(false)
                    }
                } else {
                    if (systemType!!.getIsRestaurant()) {
                        listPrintersViewModel.onAddSystemType(
                            SystemType(
                                1,
                                SystemTypeEnum.COMERCIO.type,
                                false
                            )
                        )
                        withContext(Dispatchers.Main) {
                            setSystemTypeCard(false)
                        }
                    }
                }

            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun updateToRestaurant() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (systemType == null) {
                    listPrintersViewModel.onAddSystemType(
                        SystemType(
                            1,
                            SystemTypeEnum.RESTAURANTE.type,
                            true
                        )
                    )
                    withContext(Dispatchers.Main) {
                        setSystemTypeCard(true)
                    }
                } else {
                    if (!systemType!!.getIsRestaurant()) {
                        listPrintersViewModel.onAddSystemType(
                            SystemType(
                                1,
                                SystemTypeEnum.RESTAURANTE.type,
                                true
                            )
                        )
                        withContext(Dispatchers.Main) {
                            setSystemTypeCard(true)
                        }
                    }
                }

            } catch (e: Exception) {

            }
        }
    }

    private fun initListeners() {
        binding.fabAddPrinter.setOnClickListener {
            val intent = Intent(this, AdvanceAddPrinterActivity::class.java)
            startActivity(intent)
        }
        binding.topAppBar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.miHelp -> {
                    try {
                        val helpIntent = Intent(this, HelpPrinterActivity::class.java)
                        startActivity(helpIntent)
                    } catch (e: Exception) {
                        println(e)
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun setSystemTypeCard(params: Boolean) {
        if (params) {
            binding.cvRestaurant.setCardBackgroundColor(resources.getColor(R.color.primary))
            binding.ivRestaurant.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            binding.tvRestaurant.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )

            binding.cvShop.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
            binding.ivShop.setColorFilter(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )
            binding.tvShop.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )

        } else {
            binding.cvShop.setCardBackgroundColor(resources.getColor(R.color.primary))
            binding.ivShop.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            binding.tvShop.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )

            binding.cvRestaurant.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
            binding.ivRestaurant.setColorFilter(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )
            binding.tvRestaurant.setTextColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.darker_gray
                )
            )
        }
    }

    private fun resetSystemTypeCards() {
        binding.cvRestaurant.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
        binding.ivRestaurant.setColorFilter(
            ContextCompat.getColor(
                this,
                android.R.color.darker_gray
            )
        )
        binding.tvRestaurant.setTextColor(
            ContextCompat.getColor(
                this,
                android.R.color.darker_gray
            )
        )

        binding.cvShop.setCardBackgroundColor(resources.getColor(android.R.color.transparent))
        binding.ivShop.setColorFilter(
            ContextCompat.getColor(
                this,
                android.R.color.darker_gray
            )
        )
        binding.tvShop.setTextColor(
            ContextCompat.getColor(
                this,
                android.R.color.darker_gray
            )
        )
    }
}
package com.example.printermobile.ui.Views

import android.content.Intent
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.View
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
import com.example.printermobile.ui.Views.Printer.SwipeHelper
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
            CoroutineScope(Dispatchers.IO).launch {
                listPrintersViewModel.onDeletePrinter(it.id!!)
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
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
        resetSystemTypeCards()
        listPrintersViewModel.systemType.observe(this, Observer { system ->
            if (system == null) {
                resetSystemTypeCards()
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
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.rvPrinterList) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                var buttons = listOf<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons = listOf(deleteButton)
                return buttons
            }
        })

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
                    listPrintersViewModel.onDeletePrinter(printers[viewHolder.adapterPosition].id!!)
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
//        itemTouchHelper.attachToRecyclerView(binding.rvPrinterList)


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
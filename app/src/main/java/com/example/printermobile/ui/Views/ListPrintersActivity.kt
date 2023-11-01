package com.example.printermobile.ui.Views


import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erkutaras.showcaseview.ShowcaseManager
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
        Handler().postDelayed({
            if (!getSharedPreferences("tour", MODE_PRIVATE).getBoolean("list-tour", false)) {
                getTour().build().show()
            }
        }, 10)
    }

    private fun redirect(id: Int) {
        val updatePrinterIntent = Intent(this, UpdatePrinterActivity::class.java)
        updatePrinterIntent.putExtra("printer", id.toString())
        startActivity(updatePrinterIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ShowcaseManager.REQUEST_CODE_SHOWCASE && resultCode == RESULT_OK) {
            getSharedPreferences("tour", MODE_PRIVATE).edit().putBoolean("list-tour", true).apply()
        }
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
                R.id.miTourPlay -> {
                    getTour().build().show()
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
                    R.color.textPrimary
                )
            )
            binding.tvShop.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textPrimary
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
                    R.color.textPrimary
                )
            )
            binding.tvRestaurant.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.textPrimary
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

    private fun getTour(): ShowcaseManager.Builder {
        val help = binding.topAppBar.allViews.toSet().toTypedArray()[4]
        val tourPlay = binding.topAppBar.allViews.toSet().toTypedArray()[3]
        val builder = ShowcaseManager.Builder()
        builder.context(this)
            .key("KEY")
            .view(binding.clBusinessType)
            .developerMode(true)
            .descriptionTitle("Tipo de negocio")
            .descriptionText("En esta sección podra configurar su tipo de negocio")
            .buttonText("Siguiente")
            .roundedRectangle()
            .marginFocusArea(5)
            .cancelButtonColor(Color.GREEN)
            .selectedMoveButtonColor(Color.GREEN)
            .unSelectedMoveButtonColor(Color.RED)
            .add()

            .view(binding.fabAddPrinter)
            .roundedRectangle()
            .marginFocusArea(5)
            .descriptionTitle("Agregar impresora")
            .descriptionText("Con este boton podra configurar sus impresoras")
            .buttonText("Siguiente")
            .moveButtonsVisibility(true)
            .add()

            .view(tourPlay)
            .roundedRectangle()
            .marginFocusArea(5)
            .descriptionTitle("Tour")
            .descriptionText("Esta es la sección del tour, aquí podrá repetir el tour si tiene dudas")
            .buttonText("Siguiente")
            .moveButtonsVisibility(true)
            .add()

            .view(help)
            .roundedRectangle()
            .marginFocusArea(5)
            .descriptionTitle("Ayuda")
            .descriptionText("Esta es la sección de ayuda, aquí encontrará recursos que le ayuden a solucionar su problema")
            .buttonText("Finalizar")
            .moveButtonsVisibility(true)
            .add()
        return builder
    }
}
package com.example.printermobile.ui.Views

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.allViews
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.erkutaras.showcaseview.ShowcaseManager
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityAdvanceAddPrinterBinding
import com.example.printermobile.ui.Views.advance.AdvancePrinterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdvanceAddPrinterActivity : AppCompatActivity() {
    private val advancePrinterViewModel by viewModels<AdvancePrinterViewModel>()
    private lateinit var binding: ActivityAdvanceAddPrinterBinding
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true
    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms[Manifest.permission.BLUETOOTH_CONNECT] == true
        } else true

        if (canEnableBluetooth && !isBluetoothEnabled) {
            enableBluetoothLauncher.launch(
                Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvanceAddPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initUI()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }else{
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
        }
        advancePrinterViewModel.progression.observe(this, Observer {
            if (it > 0) {
                val x = (it * 100) / 4
                binding.lpiWizardState.setProgress(x, true)
            }
        })
    }

    private fun initListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            val intent = Intent(this, ListPrintersActivity::class.java)
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

    private fun initUI() {
        binding.topAppBar.menu.removeItem(R.id.miTourPlay)
    }

    private fun getTour(): ShowcaseManager.Builder {
        val help = binding.topAppBar.allViews.toSet().toTypedArray()[4]
        val tourPlay = binding.topAppBar.allViews.toSet().toTypedArray()[3]
        val builder = ShowcaseManager.Builder()
//        builder.context(this)
//            .key("KEY")
//            .view(binding.clBusinessType)
//            .developerMode(true)
//            .descriptionTitle("Tipo de negocio")
//            .descriptionText("En esta sección podra configurar su tipo de negocio")
//            .buttonText("Siguiente")
//            .cancelButtonColor(Color.GREEN)
//            .selectedMoveButtonColor(Color.GREEN)
//            .unSelectedMoveButtonColor(Color.RED)
//            .add()
//
//            .view(binding.fabAddPrinter)
//            .circle()
//            .descriptionTitle("Agregar impresora")
//            .descriptionText("Con este boton podra configurar sus impresoras")
//            .buttonText("Siguiente")
//            .moveButtonsVisibility(true)
//            .add()

        return builder
    }
}
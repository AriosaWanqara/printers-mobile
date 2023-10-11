package com.example.printermobile.ui.Views

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.printermobile.core.print.utils.printer1.Discrimination
import com.example.printermobile.core.print.utils.printer1.PrinterBuilder
import com.example.printermobile.databinding.ActivityBackgroundPrinterBinding
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.ViewModels.BackgroundPrinterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess


@AndroidEntryPoint
class BackgroundPrinterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBackgroundPrinterBinding
    private val backgroundPrinterViewModel: BackgroundPrinterViewModel by viewModels()
    private var impresora: Printers? = null
    private var m_ambiente: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            print()
        }
    }

    private suspend fun print(): Boolean {
        var result = true;
        try {
            val intent = intent
            val uri = intent.data

            if (uri == null) {
                result = false
                return false
            }
            val data = uri.schemeSpecificPart
            val comandos = data.split("-".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            val printers = backgroundPrinterViewModel.getAll()
            m_ambiente = "comercios.illarli.com"
//            "restaurantes.illarli.com"
            if(!Discrimination(printers,m_ambiente!!,this)(comandos)){
                result = false
                val addIntent = Intent(this,AddPrinterActivity::class.java)
                startActivity(addIntent)
            }
//            var jsonObject: JSONObject?
//            for (comando in comandos) {
//                val parts = comando.split(",".toRegex()).dropLastWhile { it.isEmpty() }
//                    .toTypedArray()
//
//                var imp: PrinterBuilder? = null
//
//                when (parts[0]) {
//                    "IMPRESION_RECIBO" -> when (parts[2]) {
//                        "COMERCIOS" -> {
//                            jsonObject =
//                                Network()(parts[1], urlVentasComercios, m_ambiente!!)
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirRecibo(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    parts[2]
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//
//                        "RESTAURANTES" -> {
//                            jsonObject = Network()(
//                                parts[1],
//                                urlVentasRestaurantes,
//                                m_ambiente!!
//                            )
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirRecibo(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    parts[2]
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//                    }
//
//                    "IMPRESION_PRE_TICKET" -> {
//                        jsonObject = Network()(parts[1], urlPretickets, m_ambiente!!)
//                        imp = consultarImpresora(parts[0])
//                        if (imp != null) {
//                            imp.imprimirPreticket(
//                                jsonObject,
//                                impresora!!.copyNumber,
//                                impresora!!.charactersNumber
//                            )
//                            imp.cerrarConexion()
//                        }
//                    }
//
//                    "IMPRESION_COMANDA" -> {
//                        jsonObject = Network()(parts[1], urlComandas, m_ambiente!!)
//                        val area = jsonObject?.optString("areaImpresionPed")
//                        imp = consultarImpresora(parts[0] + ":" + area)
//                        if (imp != null) {
//                            imp.imprimirComandas(
//                                jsonObject,
//                                impresora!!.copyNumber,
//                                impresora!!.charactersNumber,
//                                area
//                            )
//                            imp.cerrarConexion()
//                        }
//                    }
//
//                    "IMPRESION_FACTURA_PRE_IMPRESA" -> when (parts[2]) {
//                        "COMERCIOS" -> {
//                            jsonObject =
//                                Network()(parts[1], urlVentasComercios, m_ambiente!!)
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirFacturPreimpresa(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    parts[2]
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//
//                        "RESTAURANTES" -> {
//                            jsonObject =
//                                Network()(parts[1], urlVentasRestaurantes, m_ambiente!!)
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirFacturPreimpresa(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    parts[2]
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//                    }
//
//                    "IMPRESION_FACTURA_ELECTRONICA" -> when (parts[2]) {
//                        "COMERCIOS" -> {
//                            jsonObject =
//                                Network()("25240631", urlVentasComercios, "comercios.illarli.com")
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirFacturaElectronica(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    "COMERCIOS"
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//
//                        "RESTAURANTES" -> {
//                            jsonObject =
//                                Network()(parts[1], urlVentasRestaurantes, m_ambiente!!)
//                            imp = consultarImpresora(parts[0])
//                            if (imp != null) {
//                                imp.imprimirFacturaElectronica(
//                                    jsonObject,
//                                    impresora!!.copyNumber,
//                                    impresora!!.charactersNumber,
//                                    parts[2]
//                                )
//                                imp.cerrarConexion()
//                            }
//                        }
//                    }
//
//                    "IMPRESION_CIERRE_CAJA" -> {
//                        jsonObject =
//                            Network()(parts[1], urlCierreCajaComercios, m_ambiente!!)
//                        imp = consultarImpresora(parts[0])
//                        if (imp != null) {
//                            imp.imprimirCierreCaja(
//                                jsonObject,
//                                impresora!!.copyNumber,
//                                impresora!!.charactersNumber
//                            )
//                            imp.cerrarConexion()
//                        }
//                    }
//
//                    "IMPRESION_COTIZACION_DETALLADA" -> {
//                        jsonObject =
//                            Network()(parts[1], urlCotizacionesComercios, m_ambiente!!)
//                        imp = consultarImpresora(parts[0])
//                        if (imp != null) {
//                            imp.imprimirCotizacionDetallada(
//                                jsonObject,
//                                impresora!!.copyNumber,
//                                impresora!!.charactersNumber
//                            )
//                            imp.cerrarConexion()
//                        }
//                    }
//
//                    "IMPRESION_COTIZACION_RESUMIDA" -> {
//                        jsonObject =
//                            Network()(parts[1], urlCotizacionesComercios, m_ambiente!!)
//                        imp = consultarImpresora(parts[0])
//                        if (imp != null) {
//                            imp.imprimirCotizacionResumida(
//                                jsonObject,
//                                impresora!!.copyNumber,
//                                impresora!!.charactersNumber
//                            )
//                            imp.cerrarConexion()
//                        }
//                    }
//
//                    "APERTURA_GAVETA" -> {
//                        imp = consultarImpresora(parts[0])
//                        if (imp != null) {
//                            imp.abrirGaveta()
//                            imp.cerrarConexion()
//                        }
//                    }
//                }
//            }
        } catch (e: InterruptedException) {
            Toast.makeText(this, "Error al imprimir", Toast.LENGTH_SHORT).show()
            result = false
            return false
        } finally {
            if (result) {
                moveTaskToBack(true);
                exitProcess(-1)
            }
        }
        return true
    }


    private suspend fun consultarImpresora(comando: String): PrinterBuilder? {
        impresora = backgroundPrinterViewModel.onCreate(comando)
        return if (impresora != null) {
            if (impresora!!.isWifi) {
                val imp = PrinterBuilder("RED")

                imp.InicializarImpresoraRed(
                    impresora!!.address,
                    impresora!!.port!!
                )
                return imp
            } else {
                if (checkBTPermissions()) {
                    val imp = PrinterBuilder("BLUETOOTH")
                    imp.InicializarImpresoraBluetooth()
                    return imp
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    private fun checkBTPermissions(): Boolean {
        return (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
                || packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
    }
}
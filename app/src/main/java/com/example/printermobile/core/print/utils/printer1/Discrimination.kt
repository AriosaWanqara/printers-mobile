package com.example.printermobile.core.print.utils.printer1

import android.content.Context
import android.content.Intent
import com.example.printermobile.domain.models.Printers
import com.example.printermobile.ui.Views.AddPrinterActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject

class Discrimination(
    private val allPrinters: List<Printers>,
    private val systemType: String,
    private val context: Context
) {
    private val urlVentasComercios = "/appWs/servicio/ordenVenta?idOrdenVenta="
    private val urlVentasRestaurantes = "/appWs/servicio/ventaPedido?idPedido="
    private val urlComandas = "/appWs/servicio/comanda?idComanda="
    private val urlPretickets = "/appWs/servicio/preTicketPedido?idPedido="
    private val urlCotizacionesComercios = "/appWs/servicio/cotizacion?idCotizacion="
    private val urlCierreCajaComercios = "/appWs/servicio/cierreCaja?idCierreCaja="

    private var printer: Printers? = null
    private var printerBuilder: PrinterBuilder? = null

    private fun setup(document: String) {
        printer = allPrinters.find {
            it.documentType == document
        }
        if (printer != null) {
            if (printer!!.isWifi) {
                printerBuilder = PrinterBuilder("RED")
                printerBuilder!!.InicializarImpresoraRed(
                    printer!!.address,
                    printer!!.port!!
                )
            } else {
                printerBuilder = PrinterBuilder("BLUETOOTH")
                printerBuilder!!.InicializarImpresoraBluetooth()
            }
        }
    }

    operator fun invoke(commands: Array<String>): Boolean {
        var jsonObject: JSONObject?
        var errorCount = 0
        var errorCommand:Array<String> = arrayOf()
        for (command in commands) {
            val parts = command.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            setup(parts[0])
            when (parts[0]) {
                "IMPRESION_RECIBO" -> when (parts[2]) {
                    "COMERCIOS" -> {
                        jsonObject =
                            Network()(parts[1], urlVentasComercios, systemType)
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirRecibo(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                parts[2]
                            )
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }

                    "RESTAURANTES" -> {
                        jsonObject = Network()(
                            parts[1],
                            urlVentasRestaurantes,
                            systemType
                        )
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirRecibo(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                parts[2]
                            )
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }
                }

                "IMPRESION_PRE_TICKET" -> {
                    jsonObject = Network()(parts[1], urlPretickets, systemType)
                    if (printerBuilder != null) {
                        printerBuilder!!.imprimirPreticket(
                            jsonObject,
                            printer!!.copyNumber,
                            printer!!.charactersNumber
                        )
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }

                "IMPRESION_COMANDA" -> {
                    jsonObject = Network()(parts[1], urlComandas, systemType)
                    val area = jsonObject?.optString("areaImpresionPed")
                    setup("IMPRESION_COMANDA:$area")
                    if (printerBuilder != null) {
                        printerBuilder!!.imprimirComandas(
                            jsonObject,
                            printer!!.copyNumber,
                            printer!!.charactersNumber,
                            area
                        )
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }

                "IMPRESION_FACTURA_PRE_IMPRESA" -> when (parts[2]) {
                    "COMERCIOS" -> {
                        jsonObject =
                            Network()(parts[1], urlVentasComercios, systemType)
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirFacturPreimpresa(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                parts[2]
                            )
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }

                    "RESTAURANTES" -> {
                        jsonObject =
                            Network()(parts[1], urlVentasRestaurantes, systemType)
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirFacturPreimpresa(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                parts[2]
                            )
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }
                }

                "IMPRESION_FACTURA_ELECTRONICA" -> when (parts[2]) {
                    "COMERCIOS" -> {
                        jsonObject =
                            Network()(parts[1], urlVentasComercios, systemType)
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirFacturaElectronica(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                "COMERCIOS"
                            )
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }

                    "RESTAURANTES" -> {
                        jsonObject =
                            Network()(parts[1], urlVentasRestaurantes, systemType)
                        if (printerBuilder != null) {
                            printerBuilder!!.imprimirFacturaElectronica(
                                jsonObject,
                                printer!!.copyNumber,
                                printer!!.charactersNumber,
                                parts[2]
                            )
                            printerBuilder!!.cerrarConexion()
                        } else {
                            errorCount++
                            if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                        }
                    }
                }

                "IMPRESION_CIERRE_CAJA" -> {
                    jsonObject =
                        Network()(parts[1], urlCierreCajaComercios, systemType)
                    if (printerBuilder != null) {
                        printerBuilder!!.imprimirCierreCaja(
                            jsonObject,
                            printer!!.copyNumber,
                            printer!!.charactersNumber
                        )
                        printerBuilder!!.cerrarConexion()
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }

                "IMPRESION_COTIZACION_DETALLADA" -> {
                    jsonObject =
                        Network()(parts[1], urlCotizacionesComercios, systemType)
                    if (printerBuilder != null) {
                        printerBuilder!!.imprimirCotizacionDetallada(
                            jsonObject,
                            printer!!.copyNumber,
                            printer!!.charactersNumber
                        )
                        printerBuilder!!.cerrarConexion()
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }

                "IMPRESION_COTIZACION_RESUMIDA" -> {
                    jsonObject =
                        Network()(parts[1], urlCotizacionesComercios, systemType)
                    if (printerBuilder != null) {
                        printerBuilder!!.imprimirCotizacionResumida(
                            jsonObject,
                            printer!!.copyNumber,
                            printer!!.charactersNumber
                        )
                        printerBuilder!!.cerrarConexion()
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }

                "APERTURA_GAVETA" -> {
                    if (printerBuilder != null) {
                        printerBuilder!!.abrirGaveta()
                        printerBuilder!!.cerrarConexion()
                    } else {
                        errorCount++
                        if (!errorCommand.contains(command)) errorCommand = errorCommand.plus(command)
                    }
                }
            }
        }
        if (errorCommand.size > 0) {
           context.getSharedPreferences("asd",0).edit().putStringSet("Commands",errorCommand.toSet()).apply()
        }
        return errorCount == 0
    }
}
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

    private lateinit var binding: ActivityHelpPrinterBinding
    private var faQList: List<FaQ> = listOf(
        FaQ(
            "¿Cómo configurar una impresora con el Sistema Illarli?",
            "Para configurar una impresora con su sistema Illarli es necesario descargar e instalar la aplicación de Configuración de Dispositivos como se muestra en el siguiente enlace",
            "https://wanqara.com/configuracion-de-equipos-con-illarli/"
        ),
        FaQ(
            "¿Cómo configurar la firma electrónica?",
            "Para configurar la firma electrónica se debe ingresar al modulo MIS DATOS, donde se puede actualizar la información de la empresa, y al final se tiene la opción para cargar la firma, recuerde que la firma debe estar en formato p12.",
            "https://wanqara.com/configurar-mis-datos-y-carga-de-firma-electronica-illarli-comercios/"
        )
    )
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
            val listActivity = Intent(this, ListPrintersActivity::class.java)
            startActivity(listActivity)
        }
    }
}
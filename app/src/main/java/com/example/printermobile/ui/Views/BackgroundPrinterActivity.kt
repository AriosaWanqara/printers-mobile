package com.example.printermobile.ui.Views

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.printermobile.R
import com.example.printermobile.databinding.ActivityBackgroundPrinterBinding
import kotlin.system.exitProcess

class BackgroundPrinterActivity : AppCompatActivity() {

    private lateinit var binding:ActivityBackgroundPrinterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundPrinterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

//    private fun showDialog() {
//        this.dialog = Dialog(this)
//        this.dialog.setContentView(R.layout.loading_dialog)
//        this.dialog.setCancelable(false)
//        dialog.setOnShowListener {
//            hideDialog()
//        }
//        this.dialog.show()
//    }
//
//    private fun hideDialog() {
//        Thread.sleep(3000)
//        dialog.hide()
//        moveTaskToBack(true);
//        exitProcess(-1)
//    }
}
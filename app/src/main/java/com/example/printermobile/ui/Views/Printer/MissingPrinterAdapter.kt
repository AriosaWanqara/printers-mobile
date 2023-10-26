package com.example.printermobile.ui.Views.Printer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R

class MissingPrinterAdapter(
    private val documentType: List<String>,
    private val onItemSelected: (String) -> Unit,
) : RecyclerView.Adapter<MissingPrinterViewHolder>() {

    var imageViewList = listOf<Map<Int, ImageView>>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissingPrinterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_document_type, parent, false)
        return MissingPrinterViewHolder(view)
    }

    override fun getItemCount(): Int = documentType.size

    override fun onBindViewHolder(holder: MissingPrinterViewHolder, position: Int) {
        holder.render(documentType[position])
        holder.mcItemContainer.setOnClickListener {
            onItemSelected(documentType[position])
            val ivIcon = holder.ivCheckIcon
            if (imageViewList.contains(mapOf(position to ivIcon))) {
                imageViewList = imageViewList.minus(mapOf(position to ivIcon))
                ivIcon.visibility = View.GONE
            } else {
                imageViewList = imageViewList.plus(mapOf(position to ivIcon))
            }
            try {
                if (imageViewList.isNotEmpty()) {
                    imageViewList.map { it ->
                        if (it.containsKey(position)) {
                            if (it.getValue(position) == holder.ivCheckIcon) {
                                it.getValue(position).visibility = View.VISIBLE
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
        try {
            if (imageViewList.isNotEmpty()) {
                imageViewList.map { it ->
                    if (it.containsKey(position)) {
                        if (it.getValue(position) == holder.ivCheckIcon) {
                            it.getValue(position).visibility = View.VISIBLE
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println(e)
        }
    }

}
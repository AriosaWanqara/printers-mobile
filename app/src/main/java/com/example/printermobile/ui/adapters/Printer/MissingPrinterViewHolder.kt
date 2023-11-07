package com.example.printermobile.ui.adapters.Printer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.databinding.ItemDocumentTypeBinding

class MissingPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemDocumentTypeBinding.bind(view)
    val ivCheckIcon = binding.ivCheckIcon;
    val mcItemContainer = binding.mcItemContainer;
    var documentName:String? = null
    fun render(documentName: String) {
        this.documentName = documentName
        binding.tvDocumentTypeName.text = documentName
        ivCheckIcon.visibility = View.GONE
    }
}
package com.example.printermobile.ui.Views.Printer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.databinding.ItemDocumentTypeBinding

class MissingPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemDocumentTypeBinding.bind(view)
    val ivCheckIcon = binding.ivCheckIcon;
    val mcItemContainer = binding.mcItemContainer;
    fun render(documentName: String) {
        binding.tvDocumentTypeName.text = documentName
        ivCheckIcon.visibility = View.GONE
    }
}
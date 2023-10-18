package com.example.printermobile.ui.Views.Printer

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.databinding.ItemDocumentTypeBinding

class MissingPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding = ItemDocumentTypeBinding.bind(view)
    private var isSelected: Boolean = false
    fun render(documentName: String, onItemSelected: (String) -> Unit) {
        binding.tvDocumentTypeName.text = documentName
        binding.mcItemContainer.setOnClickListener {
            isSelected = !isSelected
            onItemSelected(documentName)
            setView()
        }
    }


    private fun setView(){
        if (isSelected){
//            binding.mcItemContainer.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.primary))
            binding.ivCheckIcon.visibility = View.VISIBLE
        }else{
//            binding.mcItemContainer.setCardBackgroundColor(ContextCompat.getColor(itemView.context,android.R.color.transparent))
            binding.ivCheckIcon.visibility = View.GONE
        }
    }
}
package com.example.printermobile.ui.adapters.faq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.FaQ

class HelpPrinterAdapter(private var faQList: List<FaQ>) :
    RecyclerView.Adapter<HelpPrinterViewHolder>() {

    fun updateList(newList: List<FaQ>){
        val listPrintersDiffUtils = HelperPrinterDiffUtils(faQList,newList)
        val result = DiffUtil.calculateDiff(listPrintersDiffUtils)
        faQList = newList
        result.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpPrinterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false)
        return HelpPrinterViewHolder(view)
    }

    override fun getItemCount(): Int = faQList.size

    override fun onBindViewHolder(holder: HelpPrinterViewHolder, position: Int) {

        holder.render(faQList[position])
    }
}
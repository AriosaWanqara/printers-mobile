package com.example.printermobile.ui.Views.faq

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.FaQ

class HelpPrinterViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val tv = view.findViewById<TextView>(R.id.tvQuestTitle)
    private val ib = view.findViewById<TextView>(R.id.ibExpand)
    fun render(faQ: FaQ){
        tv.text = "Policia?"
    }
}
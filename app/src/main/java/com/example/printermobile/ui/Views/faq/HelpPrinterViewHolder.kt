package com.example.printermobile.ui.Views.faq

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.FaQ

class HelpPrinterViewHolder(view:View):RecyclerView.ViewHolder(view) {
    private val tv = view.findViewById<TextView>(R.id.tvQuestTitle)
    private val ib = view.findViewById<ImageButton>(R.id.ibExpand)
    fun render(faQ: FaQ){
        tv.text = faQ.getQuest()
        ib.setImageResource(R.drawable.ic_chevron_right)
        setListeners()
    }

    private fun setListeners(){
        ib.setOnClickListener {
            Toast.makeText(it.context,"Expand",Toast.LENGTH_SHORT).show()
        }
    }
}
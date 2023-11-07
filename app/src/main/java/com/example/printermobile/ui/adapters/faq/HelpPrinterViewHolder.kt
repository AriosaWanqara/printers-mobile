package com.example.printermobile.ui.adapters.faq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.printermobile.R
import com.example.printermobile.domain.models.FaQ
import com.robertlevonyan.views.expandable.Expandable


class HelpPrinterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val expandable = view.findViewById<Expandable>(R.id.expandable);
    private val headerText = view.findViewById<TextView>(R.id.header_text);
    private val contentText = view.findViewById<TextView>(R.id.content_text);
    private val contentUrl = view.findViewById<TextView>(R.id.content_url);

    fun render(faQ: FaQ) {
//        tv.text = faQ.getQuest()
//        ib.setImageResource(R.drawable.ic_chevron_right)
        expandable.animateExpand = true
        headerText.text = faQ.getQuest()
        contentText.text = faQ.getResponse()
        contentUrl.text = faQ.getUrl()
        setListeners(faQ)
    }

    private fun setListeners(faQ: FaQ) {
//        ib.setOnClickListener {
//            Toast.makeText(it.context,"Expand",Toast.LENGTH_SHORT).show()
//        }
        expandable.expandingListener = object : Expandable.ExpandingListener {
            override fun onExpanded() {
                Log.e("View", "Expanded")
            }

            override fun onCollapsed() {
                Log.e("View", "Collapsed")
            }
        }
        contentUrl.setOnClickListener {
            try {
                val uri = Uri.parse(faQ.getUrl())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                val b = Bundle()
                b.putBoolean("new_window", true)
                startActivity(it.context,intent,b)
            }catch (e:Exception){
                Toast.makeText(it.context,"No se pudo abrir el link",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
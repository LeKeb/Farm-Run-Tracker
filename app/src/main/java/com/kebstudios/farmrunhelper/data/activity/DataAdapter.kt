package com.kebstudios.farmrunhelper.data.activity

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.kebstudios.farmrunhelper.R

class DataAdapter(private var data: List<SpannableStringBuilder>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    fun changeData(data: List<SpannableStringBuilder>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.database_entry_view, parent, false) as Button

        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.findViewById<Button>(R.id.content_view).text = data[position]
    }

    inner class ViewHolder(view: Button) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {

            }
        }
    }
}
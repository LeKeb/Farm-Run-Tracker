package com.kebstudios.farmrunhelper.data.activity

import android.app.Activity
import android.content.ContextWrapper
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.text.italic
import androidx.core.text.scale
import androidx.recyclerview.widget.RecyclerView
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRun
import java.text.SimpleDateFormat
import java.util.*

class DataAdapter(private var data: List<FarmRun>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    private val format = SimpleDateFormat("YYYY/MM/dd - HH:mm:ss.SSS", Locale.getDefault())

    fun changeData(data: List<FarmRun>) {
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
        holder.bind(data[position])
    }

    inner class ViewHolder(view: Button) : RecyclerView.ViewHolder(view) {

        private lateinit var farmRun: FarmRun

        init {
            view.setOnClickListener {
                val dialog = DataDialog().apply {
                    val bundle = Bundle()
                    bundle.putSerializable("farm_run", farmRun)

                    arguments = bundle
                }
                val activity = getActivity()
                if (activity != null) {
                    dialog.show(activity.supportFragmentManager, "ConfirmDialogFragment")
                }
            }
        }

        fun bind(run: FarmRun) {
            farmRun = run

            val str = format.format(Date(run.timestamp))
            val text = SpannableStringBuilder().apply {
                append("Farm run id #${run.id}\n")
                scale(0.7f) {italic { append("\t$str") }}

            }

            itemView.findViewById<Button>(R.id.content_view).text = text

        }

        private fun getActivity(): DataActivity? {
            var context = itemView.context

            while (context is ContextWrapper) {
                if (context is Activity) {
                    return context as DataActivity
                }
                context = context.baseContext
            }

            return null
        }

    }
}
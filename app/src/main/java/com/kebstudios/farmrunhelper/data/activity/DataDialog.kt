package com.kebstudios.farmrunhelper.data.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.scale
import androidx.fragment.app.DialogFragment
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRun
import com.kebstudios.farmrunhelper.data.FarmRunDatabase

class DataDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val content = inflater.inflate(R.layout.data_dialog, null) as ScrollView

            val db = FarmRunDatabase.getInstance(it)

            val farmRun = arguments!!["farm_run"] as FarmRun

            AsyncTask.execute {
                val items = db.farmRunItemDao().getFarmRunItemsByFarmRunId(farmRun.id)

                val text = SpannableStringBuilder()
                text.apply {

                    items.forEach {item ->
                        bold { append("${item.place} - ${item.patchType}\n") }
                        append("Status: ${item.patchStatus}\n")
                        append("Harvested: ${item.harvestAmount} ${item.harvestType}\n")
                        append("Hespori seeds: ${item.hesporiSeeds}\n")
                        append("Secateurs: ${item.secateurs}\n")
                        append("Planted: ${item.plantedType}\n")
                        append("Compost: ${item.compostType}\n")
                        append("Farming level: ${item.farmingLevel}\n\n\n")
                    }
                }

                Handler(Looper.getMainLooper()).post {
                    content.findViewById<TextView>(R.id.text_box).text = text
                }
            }

            builder.setTitle("Farm run #${farmRun.id}")
            builder.setView(content)
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
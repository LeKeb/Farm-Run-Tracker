package com.kebstudios.farmrunhelper.farm_runs

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.text.SpannableStringBuilder
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.bold
import androidx.core.text.color
import androidx.core.text.italic
import androidx.core.text.underline
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRun
import com.kebstudios.farmrunhelper.data.FarmRunDatabase
import com.kebstudios.farmrunhelper.data.FarmRunItem
import java.lang.IllegalStateException

class ConfirmDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val farmRun = arguments!!["farm_run"] as FarmRun
            val farmRunItems = arguments!!["farm_run_items"] as ArrayList<FarmRunItem>

            val content = inflater.inflate(R.layout.confirm_dialog_info_view, null) as LinearLayout
            val text = content.findViewById<TextView>(R.id.confirm_text_view)

            if (farmRunItems.size > 0) {
                val harvested = hashMapOf<String, Int>()
                val harvestAmounts = hashMapOf<String, Int>()
                val planted = hashMapOf<String, Int>()
                val statuses = hashMapOf<String, Int>()
                val composts = hashMapOf<String, Int>()
                var hesporiSeeds = 0

                val str = SpannableStringBuilder().apply {
                    farmRunItems.forEach { item ->
                        if (!item.harvestType.startsWith("-")) {
                            harvested[item.harvestType] = (harvested[item.harvestType] ?: 0) + 1
                            harvestAmounts[item.harvestType] = (harvestAmounts[item.harvestType] ?: 0) +
                                    item.harvestAmount
                        }
                        if (!item.plantedType.startsWith("-"))
                            planted[item.plantedType] = (planted[item.plantedType] ?: 0) + 1

                        if (item.patchStatus != "alive" || item.harvestAmount > 0 || item.patchType == "tree")
                            statuses[item.patchStatus] = (statuses[item.patchStatus] ?: 0) + 1

                        hesporiSeeds += item.hesporiSeeds

                        underline { bold { append("${item.place} / ") }.color(0xff00ffff.toInt()) { append("${item.patchType}\n\n") } }

                        if (!item.harvestType.startsWith("-"))
                            color(0xff00ff00.toInt()) { append("harvested: ${item.harvestType}, amount: ${item.harvestAmount}\n") }
                        else
                            color(0xffff0000.toInt()) { append("harvested: nothing\n") }

                        append("\n")

                        if (!item.plantedType.startsWith("-")) {
                            color(0xff00ff00.toInt()) { append("planted: ${item.plantedType}\n") }

                            if (!item.compostType.startsWith("-"))
                                color(0xff00ff00.toInt()) { append("compost: ${item.compostType}\n") }
                            else
                                color(0xffff0000.toInt()) { append("No compost!\n") }

                            composts[item.compostType] = (composts[item.compostType] ?: 0) + 1
                        } else {
                            color(0xffff0000.toInt()) { append("planted: nothing\n") }
                        }

                        append("\n")

                        if (item.hesporiSeeds > 0)
                            color(0xffDA70D6.toInt()) { italic { append("hespori seeds: ${item.hesporiSeeds}\n") } }

                        italic { append("farming lvl: ${item.farmingLevel}\n") }
                        italic { append("patch status: ${item.patchStatus}\n") }
                        italic { append("secateurs: ${item.secateurs}\n") }

                        append("\n---------------------------------------------------------------------\n")
                    }

                    underline { bold { color(0xffffff00.toInt()) { append("Stats for farm run\n\n") } } }
                    append("Timestamp: ${farmRun.timestamp}\n\n")
                    append("Total harvested:\n")

                    italic {
                        harvested.forEach { entry ->
                            append("\t${harvestAmounts[entry.key]} ${entry.key} from ${entry.value} patches\n")
                        }
                        append("\n")
                    }

                    if (hesporiSeeds > 0)
                        color(0xffDA70D6.toInt()) { italic { append("Total Hespori seeds: $hesporiSeeds\n\n") } }

                    append("Total planted:\n")

                    italic {
                        planted.forEach { entry ->
                            append("\t${entry.key}: ${entry.value}\n")
                        }
                        append("\n")
                    }

                    append("Compost:\n")

                    italic {
                        composts.forEach { entry ->
                            if (entry.key.startsWith("-"))
                                color(0xFFFF0000.toInt()) { append("\tNo compost in ${entry.value} patches!\n") }
                            else
                                append("\t${entry.key}: ${entry.value}\n")
                        }
                        append("\n")
                    }

                    append("Patch statuses:\n")

                    italic {
                        statuses.forEach { entry ->
                            append("\t${entry.key}: ${entry.value}\n")
                        }
                        append("\n")
                    }
                }

                text.text = str
            } else {
                text.text = "No farm run data to save"
            }

            builder.setTitle("Save farm run data")
            builder.setView(content)
                .setPositiveButton("Save") { _, _ ->
                    if (farmRunItems.size > 0) {
                        Toast.makeText(context, "Saving", Toast.LENGTH_SHORT).show()
                        saveData(farmRun, farmRunItems)
                    } else {
                        Toast.makeText(context, "Nothing to save", Toast.LENGTH_SHORT).show()
                    }
                    requireActivity().finish()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun saveData(farmRun: FarmRun, farmRunItems: List<FarmRunItem>) {
        val db = FarmRunDatabase.getInstance(context!!)

        AsyncTask.execute {
            val id = db.farmRunDao().addFarmRun(farmRun)

            db.farmRunItemDao().addFarmRunItems(farmRunItems.map {
                FarmRunItem(
                    id,
                    it.patchType,
                    it.place,
                    it.harvestType,
                    it.harvestAmount,
                    it.hesporiSeeds,
                    it.plantedType,
                    it.compostType,
                    it.farmingLevel,
                    it.secateurs,
                    it.patchStatus
                )
            })
        }

    }

}
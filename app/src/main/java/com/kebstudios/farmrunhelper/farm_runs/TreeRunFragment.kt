package com.kebstudios.farmrunhelper.farm_runs

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRunItem
import com.kebstudios.farmrunhelper.ui.NumberPicker

class TreeRunFragment : FarmRunFragment() {

    private var root: ConstraintLayout? = null
    private var patch = ""
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (root != null)
            return root

        val view = inflater.inflate(R.layout.tree_run_fragment, container, false) as ConstraintLayout

        view.findViewById<NumberPicker>(R.id.farm_lvl).apply {
            setMax(99)
            setMin(1)

            setValue(prefs.getInt("farm_lvl", 1))
        }

        view.findViewById<NumberPicker>(R.id.hespori_seeds).apply {
            setMax(5)
            setMin(0)
            setValue(0)
        }

        view.apply {
            findViewById<Spinner>(R.id.harvest_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_tree_harvest_type",0))
            findViewById<Spinner>(R.id.compost_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_tree_compost_type",0))
            findViewById<Spinner>(R.id.plant_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_tree_planted_type",0))
        }

        root = view

        return view
    }

    override fun getAsFarmRunItems(id: Long): List<FarmRunItem> {
        if (root == null) {
            return listOf(
                FarmRunItem(
                    id,
                    "tree",
                    patch,
                    "---",
                    0,
                    0,
                    "---",
                    "---",
                    1,
                    false,
                    "alive"
                )
            )
        }

        return listOf(
            FarmRunItem(
                id,
                "tree",
                patch,
                root!!.findViewById<Spinner>(R.id.harvest_spinner).selectedItem.toString(),
                0,
                root!!.findViewById<NumberPicker>(R.id.hespori_seeds).getValue(),
                root!!.findViewById<Spinner>(R.id.plant_spinner).selectedItem.toString(),
                root!!.findViewById<Spinner>(R.id.compost_spinner).selectedItem.toString(),
                root!!.findViewById<NumberPicker>(R.id.farm_lvl).getValue(),
                false,
                root!!.findViewById<RadioButton>(root!!.findViewById<RadioGroup>(R.id.status_group).checkedRadioButtonId).text.toString().toLowerCase()
            )
        ).also {
            prefs.edit {
                if (it[0].plantedType != it[0].harvestType || it[0].plantedType[0] != '-' || it[0].patchStatus != "alive")
                    putInt("farm_lvl", root!!.findViewById<NumberPicker>(R.id.farm_lvl).getValue())
                putInt("${patch.toLowerCase()}_tree_harvest_type", root!!.findViewById<Spinner>(R.id.harvest_spinner).selectedItemPosition)
                putInt("${patch.toLowerCase()}_tree_compost_type", root!!.findViewById<Spinner>(R.id.compost_spinner).selectedItemPosition)
                putInt("${patch.toLowerCase()}_tree_planted_type", root!!.findViewById<Spinner>(R.id.plant_spinner).selectedItemPosition)
            }
        }
    }

    companion object {

        fun create(patch: String, prefs: SharedPreferences): TreeRunFragment {
            return TreeRunFragment().apply {
                this.patch = patch
                this.prefs = prefs
            }
        }

    }
}
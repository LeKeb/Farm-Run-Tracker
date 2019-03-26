package com.kebstudios.farmrunhelper.farm_runs

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRunItem
import com.kebstudios.farmrunhelper.ui.NumberPicker

class HerbRunFragment : FarmRunFragment() {

    enum class HerbFragmentType {
        HERB_ONLY,
        HERB_AND_FLOWER
    }

    private var root: View? = null
    private var type = HerbFragmentType.HERB_ONLY
    private var patch = ""
    private lateinit var herbPatch: ConstraintLayout
    private lateinit var flowerPatch: ConstraintLayout
    private lateinit var prefs: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (root != null)
            return root

        val view: View =
            if (type == HerbFragmentType.HERB_ONLY) {
                inflater.inflate(R.layout.herb_run_fragment_1, container, false)
            } else {
                val v = inflater.inflate(R.layout.herb_run_fragment_2, container, false)
                flowerPatch = v.findViewById<ConstraintLayout>(R.id.flower_patch).apply {
                    findViewById<NumberPicker>(R.id.harvest_amount_flower).apply {
                        setMax(100)
                        setMin(0)
                        setValue(prefs.getInt("${patch.toLowerCase()}_flower_harvest", 0))
                    }
                    findViewById<NumberPicker>(R.id.farm_lvl_flower).apply {
                        setMax(99)
                        setMin(1)
                        setValue(prefs.getInt("farm_lvl", 1))
                    }
                    findViewById<NumberPicker>(R.id.hespori_seeds_flower).apply {
                        setMax(5)
                        setMin(0)
                        setValue(0)
                    }
                    findViewById<Spinner>(R.id.harvest_spinner_flower).apply {
                        adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.flower_types))
                        setSelection(prefs.getInt("${patch.toLowerCase()}_flower_harvest_type",0))
                    }
                    findViewById<Spinner>(R.id.plant_spinner_flower).apply {
                        adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.flower_types))
                        setSelection(prefs.getInt("${patch.toLowerCase()}_flower_planted_type",0))
                    }
                    findViewById<TextView>(R.id.patch_type_flower).text = "Flower Patch"
                    findViewById<Spinner>(R.id.compost_spinner_flower).setSelection(prefs.getInt("${patch.toLowerCase()}_flower_compost_type",0))
                    findViewById<CheckBox>(R.id.secateurs_box_flower).isChecked = prefs.getBoolean("${patch.toLowerCase()}_flower_secateurs", false)
                }
                v
            }

        herbPatch = view.findViewById<ConstraintLayout>(R.id.herb_patch).apply {
            findViewById<NumberPicker>(R.id.harvest_amount).apply {
                setMax(100)
                setMin(0)
                setValue(prefs.getInt("${patch.toLowerCase()}_herb_harvest", 0))
            }
            findViewById<NumberPicker>(R.id.farm_lvl).apply {
                setMax(99)
                setMin(1)
                setValue(prefs.getInt("farm_lvl", 1))
            }
            findViewById<NumberPicker>(R.id.hespori_seeds).apply {
                setMax(5)
                setMin(0)
                setValue(0)
            }
            findViewById<TextView>(R.id.patch_type).text = "Herb Patch"
            findViewById<Spinner>(R.id.harvest_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_herb_harvest_type",0))
            findViewById<Spinner>(R.id.plant_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_herb_planted_type",0))
            findViewById<Spinner>(R.id.compost_spinner).setSelection(prefs.getInt("${patch.toLowerCase()}_herb_compost_type",0))
            findViewById<CheckBox>(R.id.secateurs_box).isChecked = prefs.getBoolean("${patch.toLowerCase()}_herb_secateurs", false)
        }

        root = view

        return view
    }

    override fun getAsFarmRunItems(id: Long): List<FarmRunItem> {
        if (root == null) {
            return listOf(
                FarmRunItem(
                    id,
                    "herb",
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

        val list = mutableListOf(
            FarmRunItem(
                id,
                "herb",
                patch,
                herbPatch.findViewById<Spinner>(R.id.harvest_spinner).selectedItem.toString(),
                herbPatch.findViewById<NumberPicker>(R.id.harvest_amount).getValue(),
                herbPatch.findViewById<NumberPicker>(R.id.hespori_seeds).getValue(),
                herbPatch.findViewById<Spinner>(R.id.plant_spinner).selectedItem.toString(),
                herbPatch.findViewById<Spinner>(R.id.compost_spinner).selectedItem.toString(),
                herbPatch.findViewById<NumberPicker>(R.id.farm_lvl).getValue(),
                herbPatch.findViewById<CheckBox>(R.id.secateurs_box).isChecked,
                herbPatch.findViewById<RadioButton>(herbPatch.findViewById<RadioGroup>(R.id.status_group).checkedRadioButtonId).text.toString().toLowerCase()
            )
        )

        if (type == HerbFragmentType.HERB_AND_FLOWER) {
            list.add(
                FarmRunItem(
                    id,
                    "flower",
                    patch,
                    flowerPatch.findViewById<Spinner>(R.id.harvest_spinner_flower).selectedItem.toString(),
                    flowerPatch.findViewById<NumberPicker>(R.id.harvest_amount_flower).getValue(),
                    flowerPatch.findViewById<NumberPicker>(R.id.hespori_seeds_flower).getValue(),
                    flowerPatch.findViewById<Spinner>(R.id.plant_spinner_flower).selectedItem.toString(),
                    flowerPatch.findViewById<Spinner>(R.id.compost_spinner_flower).selectedItem.toString(),
                    flowerPatch.findViewById<NumberPicker>(R.id.farm_lvl_flower).getValue(),
                    flowerPatch.findViewById<CheckBox>(R.id.secateurs_box_flower).isChecked,
                    flowerPatch.findViewById<RadioButton>(flowerPatch.findViewById<RadioGroup>(R.id.status_group_flower).checkedRadioButtonId).text.toString().toLowerCase()
                )
            )

            prefs.edit {
                if (list[1].plantedType != list[1].harvestType || list[1].plantedType[1] != '-' || list[1].patchStatus != "alive")
                    putInt("farm_lvl", flowerPatch.findViewById<NumberPicker>(R.id.farm_lvl_flower).getValue())
                putInt("${patch.toLowerCase()}_flower_harvest", flowerPatch.findViewById<NumberPicker>(R.id.harvest_amount_flower).getValue())
                putInt("${patch.toLowerCase()}_flower_harvest_type", flowerPatch.findViewById<Spinner>(R.id.harvest_spinner_flower).selectedItemPosition)
                putInt("${patch.toLowerCase()}_flower_planted_type", flowerPatch.findViewById<Spinner>(R.id.plant_spinner_flower).selectedItemPosition)
                putInt("${patch.toLowerCase()}_flower_compost_type", flowerPatch.findViewById<Spinner>(R.id.compost_spinner_flower).selectedItemPosition)
                putBoolean("${patch.toLowerCase()}_flower_secateurs", flowerPatch.findViewById<CheckBox>(R.id.secateurs_box_flower).isChecked)
            }

        }

        prefs.edit {
            if (list[0].plantedType != list[0].harvestType || list[0].plantedType[0] != '-' || list[0].patchStatus != "alive")
                putInt("farm_lvl", herbPatch.findViewById<NumberPicker>(R.id.farm_lvl).getValue())
            putInt("${patch.toLowerCase()}_herb_harvest", herbPatch.findViewById<NumberPicker>(R.id.harvest_amount).getValue())
            putInt("${patch.toLowerCase()}_herb_harvest_type", herbPatch.findViewById<Spinner>(R.id.harvest_spinner).selectedItemPosition)
            putInt("${patch.toLowerCase()}_herb_planted_type", herbPatch.findViewById<Spinner>(R.id.plant_spinner).selectedItemPosition)
            putInt("${patch.toLowerCase()}_herb_compost_type", herbPatch.findViewById<Spinner>(R.id.compost_spinner).selectedItemPosition)
            putBoolean("${patch.toLowerCase()}_herb_secateurs", herbPatch.findViewById<CheckBox>(R.id.secateurs_box).isChecked)
        }

        return list
    }


    companion object {

        fun create(patch: String, type: HerbFragmentType, prefs: SharedPreferences): HerbRunFragment {
            return HerbRunFragment().apply {
                this.patch = patch
                this.type = type
                this.prefs = prefs
            }
        }

    }

}
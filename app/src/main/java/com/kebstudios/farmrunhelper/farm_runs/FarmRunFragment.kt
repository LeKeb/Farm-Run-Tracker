package com.kebstudios.farmrunhelper.farm_runs

import androidx.fragment.app.Fragment
import com.kebstudios.farmrunhelper.data.FarmRunItem

abstract class FarmRunFragment : Fragment() {

    abstract fun getAsFarmRunItems(id: Long) : List<FarmRunItem>

}
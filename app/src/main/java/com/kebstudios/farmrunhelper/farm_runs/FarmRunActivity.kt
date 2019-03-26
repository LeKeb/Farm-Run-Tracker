package com.kebstudios.farmrunhelper.farm_runs

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.os.postDelayed
import com.kebstudios.farmrunhelper.*
import com.kebstudios.farmrunhelper.data.FarmRun
import com.kebstudios.farmrunhelper.data.FarmRunItem
import kotlinx.android.synthetic.main.activity_farm_run.*
import java.util.*
import kotlin.collections.ArrayList

class FarmRunActivity : AppCompatActivity() {

    private lateinit var farmPatches: List<String>

    private lateinit var adapter: TabAdapter

    private var backPressed = 0

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farm_run)

        adapter = TabAdapter(supportFragmentManager)

        when (intent.getStringExtra("type")) {
            "herb" -> {

                title_field.text = "Herb run"

                val arr1 = resources.getStringArray(R.array.herb_flower_patches)
                val arr2 = resources.getStringArray(R.array.herb_only_patches)

                farmPatches = (arr1 + arr2).toList()

                for (place in arr1) {
                    adapter.addFragment(
                        HerbRunFragment.create(
                            place,
                            HerbRunFragment.HerbFragmentType.HERB_AND_FLOWER,
                            getPreferences(Context.MODE_PRIVATE)
                        ), place)
                }

                for (place in arr2) {
                    adapter.addFragment(
                        HerbRunFragment.create(
                            place,
                            HerbRunFragment.HerbFragmentType.HERB_ONLY,
                            getPreferences(Context.MODE_PRIVATE)
                        ), place)
                }
            }
            "tree" -> {

                title_field.text = "Tree run"

                farmPatches = resources.getStringArray(R.array.tree_patches).toList()

                for (place in farmPatches) {
                    adapter.addFragment(TreeRunFragment.create(place, getPreferences(Context.MODE_PRIVATE)), place)
                }
            }
            else -> {
                throw IllegalArgumentException("Invalid extra \"type\": ${intent.getStringExtra("type")}")
            }
        }





        pager.adapter = adapter
        tabs.setupWithViewPager(pager)

        save_button.setOnClickListener {
            showSaveDialog()
        }
    }

    override fun onBackPressed() {
        synchronized(adapter) {
            if (backPressed == 0) {
                backPressed++
                handler.postDelayed(2000L) {
                    synchronized(adapter) {
                        backPressed = 0
                    }
                }
                Toast.makeText(this, "Press again to exit without saving", Toast.LENGTH_SHORT).show()
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun showSaveDialog() {

        val farmRun = FarmRun(timestamp = Date().time)
        val items = mutableListOf<FarmRunItem>()

        farmPatches.forEach {
            val fragment = adapter.getFragment(it) as FarmRunFragment

            val fragItems = fragment.getAsFarmRunItems(farmRun.id)

            for (i in fragItems) {
                if ((i.plantedType != i.harvestType) || i.plantedType[0] != '-' || i.patchStatus != "alive")
                    items.add(i)
            }

        }

        val dialog = ConfirmDialog().apply {
            val bundle = Bundle()
            bundle.putSerializable("farm_run", farmRun)
            bundle.putSerializable("farm_run_items", ArrayList<FarmRunItem>(items))

            arguments = bundle
        }
        dialog.show(supportFragmentManager, "ConfirmDialogFragment")
    }



}

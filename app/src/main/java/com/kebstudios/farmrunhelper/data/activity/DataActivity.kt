package com.kebstudios.farmrunhelper.data.activity

import android.os.AsyncTask
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.italic
import androidx.core.text.scale
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kebstudios.farmrunhelper.R
import com.kebstudios.farmrunhelper.data.FarmRunDatabase
import kotlinx.android.synthetic.main.activity_data.*
import java.text.SimpleDateFormat
import java.util.*

class DataActivity : AppCompatActivity() {

    private lateinit var mAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val db = FarmRunDatabase.getInstance(this)

        mAdapter = DataAdapter(listOf())
        val lm = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recycler_view.apply {
            adapter = mAdapter
            layoutManager = lm
        }

        AsyncTask.execute {
            val runs = db.farmRunDao().getAllFarmRuns().sortedBy { -it.id }

            runOnUiThread {
                mAdapter.changeData(runs)
            }
        }
    }

}
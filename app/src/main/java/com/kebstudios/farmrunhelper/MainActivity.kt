package com.kebstudios.farmrunhelper

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kebstudios.farmrunhelper.data.FarmRunDatabase
import com.kebstudios.farmrunhelper.data.activity.DataActivity
import com.kebstudios.farmrunhelper.farm_runs.FarmRunActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)

            herb_run_button.setOnClickListener {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
            }

            tree_run_button.setOnClickListener {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
            }

            data_button.setOnClickListener {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST)
            }
        } else {
            init()
        }

    }

    private fun init() {

        herb_run_button.setOnClickListener {
            val intent = Intent(this, FarmRunActivity::class.java)
            intent.putExtra("type", "herb")
            startActivity(intent)
        }

        tree_run_button.setOnClickListener {
            val intent = Intent(this, FarmRunActivity::class.java)
            intent.putExtra("type", "tree")
            startActivity(intent)
        }

        data_button.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    Toast.makeText(this, "This app needs storage permission for saving data", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {

            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST = 8422
    }
}

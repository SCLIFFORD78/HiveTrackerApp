package org.wit.hivetrackerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.ActivityHivetrackerBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber.i

class HiveTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHivetrackerBinding
    var hive = HiveModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHivetrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp
        i("Hive Activity started...")
        binding.btnAdd.setOnClickListener() {
            hive.title = binding.hiveTitle.text.toString()
            hive.description = binding.description.text.toString()
            if (hive.title.isNotEmpty()) {
                app.hives.add(hive.copy())
                i("add Button Pressed: ${hive}")
                for (i in app.hives.indices)
                { i("Hive[$i]:${this.app.hives[i]}") }
            }
            else {
                Snackbar.make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
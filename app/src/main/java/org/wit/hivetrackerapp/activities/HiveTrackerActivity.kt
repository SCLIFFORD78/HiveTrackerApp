package org.wit.hivetrackerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.ActivityHivetrackerBinding
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber
import timber.log.Timber.i

class HiveTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHivetrackerBinding
    var hive = HiveModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHivetrackerBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_hivetracker)

        Timber.plant(Timber.DebugTree())

        i("Hive Activity started..")

        binding.btnAdd.setOnClickListener() {
            hive.title = binding.hiveTitle.text.toString()
            if (hive.title.isNotEmpty()) {
                i("add Button Pressed: ${hive.title}")
            }
            else {
                Snackbar
                    .make(it,"Please Enter a title", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
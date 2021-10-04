package org.wit.hivetrackerapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        var edit = false

        binding = ActivityHivetrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp
        i("Hive Activity started...")

        if (intent.hasExtra("hive_edit")) {
            edit = true
            hive = intent.extras?.getParcelable("hive_edit")!!
            binding.hiveTitle.setText(hive.title)
            binding.description.setText(hive.description)
            binding.btnAdd.setText(R.string.save_hive)
        }

        binding.btnAdd.setOnClickListener() {
            hive.title = binding.hiveTitle.text.toString()
            hive.description = binding.description.text.toString()
            if (hive.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_hive_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit){
                    app.hives.update(hive.copy())
                }else{
                    app.hives.create(hive.copy())
                }
            }
            i("add Button Pressed: $hive")
            setResult(RESULT_OK)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_hive, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
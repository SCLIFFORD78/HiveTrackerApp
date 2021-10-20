package org.wit.hivetrackerapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.ActivityHivetrackerBinding
import org.wit.hivetrackerapp.helpers.showImagePicker
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber.i

class HiveTrackerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHivetrackerBinding
    var hive = HiveModel()
    lateinit var app: MainApp

    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerImagePickerCallback()

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
            binding.chooseImage.setText(R.string.button_changeImage)
            Picasso.get()
                .load(hive.image)
                .into(binding.hiveImage)
        }

        binding.btnAdd.setOnClickListener() {
            hive.title = binding.hiveTitle.text.toString()
            hive.description = binding.description.text.toString()
            if (hive.title.isEmpty()) {
                Snackbar.make(it, R.string.enter_hive_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.hives.update(hive.copy())
                } else {
                    app.hives.create(hive.copy())
                }
            }
            i("add Button Pressed: $hive")
            setResult(RESULT_OK)
            finish()
        }
        binding.chooseImage.setOnClickListener {
            i("Select image")
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        binding.hiveLocation.setOnClickListener {
            i ("Set Location Pressed")
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

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            hive.image = result.data!!.data!!
                            Picasso.get()
                                .load(hive.image)
                                .into(binding.hiveImage)
                        } // end of if
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
    }

}

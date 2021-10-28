package org.wit.hivetrackerapp.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.FragmentAddBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber.i
import org.wit.hivetrackerapp.helpers.showImagePicker
import android.content.Intent
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.parcelize.Parcelize
import org.wit.hivetrackerapp.activities.MapsActivity
import org.wit.hivetrackerapp.databinding.ActivityMapsBinding
import org.wit.hivetrackerapp.databinding.HomeBinding
import org.wit.hivetrackerapp.models.Location

var hive = HiveModel()

class AddFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentAddBinding? = null
    private val fragBinding get() = _fragBinding!!
    var edit = false
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var location = Location()
    lateinit var data: HiveModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        data = HiveModel()
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (container != null) {
            container.removeAllViews();
        }
        _fragBinding = FragmentAddBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_add)

        edit = false
        var bundle = arguments
        //hive = data

        if (bundle != null) {
            //hive = data
            hive = bundle?.get("data") as HiveModel
            edit = true
            fragBinding.hiveTitle.setText(hive.title)
            fragBinding.description.setText(hive.description)
            fragBinding.btnAdd.setText(R.string.save_hive)
            fragBinding.chooseImage.setText(R.string.button_changeImage)
            Picasso.get()
                .load(hive.image)
                .into(fragBinding.hiveImage)
        }
        setAddButtonListener(fragBinding)
        setChooseImageListener(fragBinding)
        registerImagePickerCallback(fragBinding)
        setChooseMapListener(fragBinding)
        registerMapCallback(fragBinding)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    fun setAddButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            hive.title = layout.hiveTitle.text.toString()
            hive.description = layout.description.text.toString()
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
            //setResult(RESULT_OK)
            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
        }
    }

    fun setChooseImageListener(layout: FragmentAddBinding){
        layout.chooseImage.setOnClickListener{
            showImagePicker(imageIntentLauncher)
        }
    }

    fun setChooseMapListener(layout: FragmentAddBinding){
        layout.hiveLocation.setOnClickListener{
            i ("Set Location Pressed")
            var location = Location(52.0634310, -9.6853542, 15f)
            if (hive.zoom != 0f) {
                location.lat =  hive.lat
                location.lng = hive.lng
                location.zoom = hive.zoom
            }
            val launcherIntent = Intent(activity,MapsActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }
    }

    private fun registerImagePickerCallback(layout: FragmentAddBinding) {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    -1 -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            hive.image = result.data!!.data!!
                            Picasso.get()
                                .load(hive.image)
                                .into(layout.hiveImage)
                        } // end of if
                    }
                    0 -> {
                    }
                    else -> {
                        i("Image selection cancelled")
                    }
                }
            }
    }

    private fun registerMapCallback(layout: FragmentAddBinding) {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                i("Map data ${result.data.toString()}")
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            location = result.data!!.extras?.getParcelable("location")!!
                            hive.lat = location.lat
                            hive.lng = location.lng
                            hive.zoom = location.zoom
                            i("Location == $location")
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
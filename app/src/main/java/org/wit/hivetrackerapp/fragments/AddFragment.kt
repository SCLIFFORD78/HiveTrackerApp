package org.wit.hivetrackerapp.fragments

import android.app.Activity.RESULT_CANCELED
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    lateinit var spinner: Spinner



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
        container?.removeAllViews()
        _fragBinding = FragmentAddBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_add)
        spinner = root.findViewById(R.id.hiveTypeSpinner)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hiveType, android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            adapter.also { spinner.adapter = it }
        }
        fragBinding.hiveTitle.setText(app.hives.getTag().toString())

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hive, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
        return super.onOptionsItemSelected(item)
    }

    fun setAddButtonListener(layout: FragmentAddBinding) {
        layout.btnAdd.setOnClickListener {
            hive.tag = layout.hiveTitle.text.toString().toLong()
            hive.description = layout.description.text.toString()
            hive.type = spinner.selectedItem.toString()
            hive.userID = app.loggedInUser.id
            app.hives.create(hive.copy())
            Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
            i("Add Hive Button Pressed: ${app.loggedInUser.userName}")
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
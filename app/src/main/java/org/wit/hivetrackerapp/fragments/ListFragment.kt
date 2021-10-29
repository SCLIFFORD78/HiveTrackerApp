package org.wit.hivetrackerapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.adapters.HiveTrackerAdapter
import org.wit.hivetrackerapp.databinding.FragmentAddBinding
import org.wit.hivetrackerapp.databinding.FragmentListBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import org.wit.hivetrackerapp.models.UserModel
import timber.log.Timber







class ListFragment : Fragment(), HiveTrackerAdapter.OnHiveClickListener {
    lateinit var app: MainApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var comm: HiveTrackerAdapter.Communicator
    lateinit var spinner: Spinner
    lateinit var spinner2: Spinner
    private lateinit var users : List<UserModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_list)

        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = HiveTrackerAdapter(app.hives.findAll(),this)

        users = app.users.findAll()
        val names: MutableList<String> = arrayListOf()
        for (user in users){
            names.add(0,(user.firstName+" "+user.secondName))
        }
        names.add(0,"All Users")

        spinner = root.findViewById(R.id.hiveTypeSpinnerSearch)
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hiveTypeSearch, android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            adapter.also { spinner.adapter = it }
        }
        spinner2 = root.findViewById(R.id.hiveOwnerSpinnerSearch)
        // Creating adapter for spinner
        val dataAdapter: ArrayAdapter<String>? =
            activity?.let { ArrayAdapter<String>(it.applicationContext, android.R.layout.simple_spinner_item, names) }
        dataAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinner2.adapter = dataAdapter


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hiveTypeSearch, android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            adapter.also { spinner.adapter = it }
        }
        setUpdateSearchButtonListener(fragBinding)
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ListFragment().apply {
                arguments = Bundle().apply { }
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
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    override fun onHiveClick(position: Int) {

        val clickedItem = app.hives.find(app.hives.findAll()[position])
        Timber.i("Item Pressed: $clickedItem clicked")
        comm = requireActivity() as HiveTrackerAdapter.Communicator

        Navigation.findNavController(this.requireView()).navigate(R.id.updateFragment)
        if (clickedItem != null) {
            comm.passDataCom(clickedItem)
        }
        //Navigation.findNavController(this.requireView()).navigate(R.id.updateFragment)
        //val launcherIntent = Intent(activity, AddFragment::class.java)
        //launcherIntent.putExtra("hive_edit", clickedItem)
    }

    fun setUpdateSearchButtonListener(layout: FragmentListBinding) {
        layout.btnUpdateSearch.setOnClickListener {
            val type = spinner.selectedItem.toString()
            val position = spinner2.selectedItemPosition
            val returnedHiveTypes:List<HiveModel> = app.hives.findByType(type)
            val returnedHiveUserID:List<HiveModel> = app.hives.findByOwner(this. users[position-1].id)
            if (type != "All Hive Types") {
                fragBinding.recyclerView.adapter = HiveTrackerAdapter(returnedHiveTypes, this)
            }else{
                fragBinding.recyclerView.adapter = HiveTrackerAdapter(app.hives.findAll(),this)
            }
        }
    }




}
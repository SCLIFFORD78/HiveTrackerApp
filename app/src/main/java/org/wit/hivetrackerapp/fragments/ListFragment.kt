package org.wit.hivetrackerapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.view.*
import android.widget.Adapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.activities.Home
import org.wit.hivetrackerapp.adapters.HiveTrackerAdapter
import org.wit.hivetrackerapp.databinding.FragmentAddBinding
import org.wit.hivetrackerapp.databinding.FragmentListBinding
import org.wit.hivetrackerapp.databinding.HomeBinding
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import timber.log.Timber

class ListFragment : Fragment(), HiveTrackerAdapter.OnHiveClickListener {
    lateinit var app: MainApp
    private var _fragBinding: FragmentListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var comm: HiveTrackerAdapter.Communicator


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

        val clickedItem = app.hives.findAll()[position]
        Timber.i("Item Pressed: $clickedItem clicked")
        comm = requireActivity() as HiveTrackerAdapter.Communicator

        Navigation.findNavController(this.requireView()).navigate(R.id.updateFragment)
        comm.passDataCom(clickedItem)
        //Navigation.findNavController(this.requireView()).navigate(R.id.updateFragment)
        //val launcherIntent = Intent(activity, AddFragment::class.java)
        //launcherIntent.putExtra("hive_edit", clickedItem)
    }


}
package org.wit.hivetrackerapp.fragments

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



class AddFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentAddBinding? = null
    private val fragBinding get() = _fragBinding!!
    val hive = HiveModel()
    var edit = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
        edit = false

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentAddBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_add)
        setAddButtonListener(fragBinding)
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
}
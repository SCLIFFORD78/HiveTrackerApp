package org.wit.hivetrackerapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.FragmentLoginBinding
import org.wit.hivetrackerapp.databinding.FragmentLoginOrRegisterBinding
import org.wit.hivetrackerapp.main.MainApp
import timber.log.Timber

class LoginOrRegisterFragment : Fragment() {
    lateinit var app: MainApp
    private var _fragBinding: FragmentLoginOrRegisterBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentLoginOrRegisterBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        //activity?.title = getString(R.string.fragment_header_home)
        setLoginButtonListener(fragBinding)
        setRegisterButtonListener(fragBinding)
        return root
    }

    fun setLoginButtonListener(layout: FragmentLoginOrRegisterBinding) {
        layout.loginOption .setOnClickListener {
            Navigation.findNavController(this.requireView()).navigate(R.id.loginFragment2)
            Timber.i("Login option button pressed Button Pressed")
        }
    }

    fun setRegisterButtonListener(layout: FragmentLoginOrRegisterBinding) {
        layout.registerOption .setOnClickListener {
            Navigation.findNavController(this.requireView()).navigate(R.id.registerFragment)
            Timber.i("Register option button pressed Button Pressed")
        }
    }
}
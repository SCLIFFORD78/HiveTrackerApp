package org.wit.hivetrackerapp.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import org.wit.hivetrackerapp.databinding.FragmentLoginBinding

import org.wit.hivetrackerapp.R
import org.wit.hivetrackerapp.databinding.FragmentAddBinding
import org.wit.hivetrackerapp.fragments.hive
import org.wit.hivetrackerapp.main.MainApp
import org.wit.hivetrackerapp.models.HiveModel
import org.wit.hivetrackerapp.models.UserModel
import timber.log.Timber
private var user= UserModel()
class LoginFragment : Fragment() {
    private val _loginForm = MutableLiveData<LoginFormState<Any?>>()
    lateinit var app: MainApp
    private var _fragBinding: FragmentLoginBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentLoginBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_add)
        setLoginButtonListener(fragBinding)
        return root
    }



    private fun updateUiWithUser(username: String) {
        val welcome = getString(R.string.welcome) + username
        // TODO : initiate successful logged in experience
        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)

        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return username.length > 5
        //return if (username.contains("@")) {
            //Patterns.EMAIL_ADDRESS.matcher(username).matches()
        //} else {
           // username.isNotBlank()
        //}
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun setLoginButtonListener(layout: FragmentLoginBinding) {
        layout.login.setOnClickListener {
            var test = user
            user.userName = layout.username.text.toString()
            user.password = layout.password.text.toString()
            if (!isUserNameValid(user.userName)) {
                showLoginFailed(R.string.invalid_username)
            } else if (!isPasswordValid(user.password)) {
                showLoginFailed(R.string.invalid_password)
            } else {

                var registeredUser = app.users.find(user)
                if (registeredUser != null) {
                    if (registeredUser.userName.equals(user.userName)){
                        updateUiWithUser(user.userName)
                        app.loggedInUser = user
                        Navigation.findNavController(this.requireView()).navigate(R.id.listFragment)
                    }
                }else{
                    showLoginFailed(R.string.login_failed)
                }
            }

            Timber.i("add Button Pressed: $user")
            //setResult(RESULT_OK)

        }
    }


}
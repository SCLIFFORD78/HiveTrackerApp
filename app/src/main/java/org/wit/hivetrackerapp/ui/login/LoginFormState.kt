package org.wit.hivetrackerapp.ui.login

import org.wit.hivetrackerapp.models.UserModel

/**
 * Data validation state of the login form.
 */

data class LoginFormState<T>(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val firstNameError: Int? = null,
    val secondNameError: Int? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false,
    val user: UserModel
)
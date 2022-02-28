package com.developeros.languageproducer.model.Authenticaiton

import android.text.LoginFilter

class LoginUserModel {
    var UserEmail: String
    var UserPassword: String

    constructor(Username: String, UserPassword: String) {
        this.UserEmail = Username
        this.UserPassword = UserPassword
    }
}
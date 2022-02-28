package com.developeros.languageproducer.model.Authenticaiton

class RegisterUserModel {
    var Username: String
    var UserEmail: String
    var UserPassword: String
    constructor(Username: String, UserEmail: String, UserPassword: String) {
        this.Username = Username
        this.UserEmail = UserEmail
        this.UserPassword = UserPassword
    }
}
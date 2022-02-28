package com.developeros.languageproducer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.model.Authenticaiton.LoginUserModel
import com.developeros.languageproducer.model.Authenticaiton.RegisterUserModel
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class AuthenticaitionViewModel(val app:Languageproducer):ViewModel() {

    var RegisterState: LiveData<FirebaseUser>? =app.authenticationRepository.RegisterState
    fun RegisterUserToFirebase(registerUserModel: RegisterUserModel)=viewModelScope.launch(Dispatchers.IO) {
        app.authenticationRepository.RegisterUserToFireBase(registerUserModel,app)
    }
    var LoginState: LiveData<FirebaseUser>? =app.authenticationRepository.LoginState
    fun LoginUser(loginUserModel: LoginUserModel) =viewModelScope.launch(Dispatchers.IO) {
        app.authenticationRepository.LoginUser(loginUserModel,app)
    }
    fun SignOut()=viewModelScope.launch(Dispatchers.IO) {
        app.authenticationRepository.SignOut(app)
    }
}
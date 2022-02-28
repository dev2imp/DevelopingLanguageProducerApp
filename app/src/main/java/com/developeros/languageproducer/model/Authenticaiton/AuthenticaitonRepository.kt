package com.developeros.languageproducer.model.Authenticaiton

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser

class AuthenticaitonRepository {
    var RegisterState: MutableLiveData<FirebaseUser> = MutableLiveData()
    var LoginState: MutableLiveData<FirebaseUser> = MutableLiveData()
    suspend fun RegisterUserToFireBase(
        registerUserModel: RegisterUserModel,
        app: Languageproducer
    ) {
        app.auth.createUserWithEmailAndPassword(
            registerUserModel.UserEmail,
            registerUserModel.UserPassword
        ).addOnCompleteListener(ContextCompat.getMainExecutor(app), OnCompleteListener {
            if (it.isSuccessful) {
                RegisterState.value = app.auth.currentUser
            }
        })
    }
    suspend fun LoginUser(loginUserModel: LoginUserModel, app: Languageproducer) {
        app.auth.signInWithEmailAndPassword(loginUserModel.UserEmail, loginUserModel.UserPassword)
            .addOnCompleteListener(ContextCompat.getMainExecutor(app), OnCompleteListener {
                if (it.isSuccessful) {
                    LoginState.value = app.auth.currentUser
                }
            })
        }
    suspend fun SignOut(app: Languageproducer){
        app.auth.signOut()
        LoginState = MutableLiveData()
    }
}
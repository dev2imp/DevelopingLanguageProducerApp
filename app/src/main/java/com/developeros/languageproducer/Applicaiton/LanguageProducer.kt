package com.developeros.languageproducer.Applicaiton
import android.app.Application
import com.developeros.languageproducer.model.Authenticaiton.AuthenticaitonRepository
import com.developeros.languageproducer.model.RoomDatabse.AppDatabase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
class Languageproducer: Application() {
    val auth by lazy {
        Firebase.auth
    }
    val applicationScope= CoroutineScope(SupervisorJob())
    val database by lazy {
        AppDatabase.getDatabase(this,applicationScope)
    }
    val authenticationRepository by lazy {
        AuthenticaitonRepository()
    }
}
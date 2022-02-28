package com.developeros.languageproducer.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.utils.GetLangsFromBlogSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var BottomNavRelLayout:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.MainViewPager) as NavHostFragment
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav)
        BottomNavRelLayout = findViewById(R.id.BottomNavRelLayout)
        var navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)
        /*
        Run Below code once ans save fetched data to singleton
        to use as we need
         */
        GlobalScope.launch(Dispatchers.IO) {
            GetLangsFromBlogSingleton.getLangs()
        }
    }
}
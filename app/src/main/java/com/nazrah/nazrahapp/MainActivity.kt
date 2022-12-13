package com.nazrah.nazrahapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nazrah.nazrahapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val parentScreen = arrayListOf<Int>()
    lateinit var bottomNavigationView: BottomNavigationView

    lateinit var mNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //load parent screens
        parentScreenList()
        setSetupBottomNavAndNavHost()

//        lifecycleScope.launch {
//            delay(5000)
//            if (FirebaseUtils.firebaseAuth.currentUser != null) {
//                val db = Firebase.firestore(Firebase.app)
//                db.collection(Constants.USERS).document(FirebaseUtils.firebaseAuth.currentUser?.uid!!).get().addOnCompleteListener {
//                    if (it.isSuccessful){
//
//
//                        if (it.result.get("type")==1L){
//                            binding.navigationId.createClass.setVisible(true)
//                            binding.navigationId.joinClassID.setVisible(false)
//                        }
//                        else if (it.result.get("type")==2L){
//                            binding.navigationId.createClass.setVisible(false)
//                            binding.navigationId.joinClassID.setVisible(true)
//                        }
//                        else
//                        {
//                            binding.navigationId.createClass.setVisible(false)
//                            binding.navigationId.joinClassID.setVisible(false)
//                        }
//                        Log.e(SplashFragment::class.java.simpleName,"Constants.USERS type "+ it.result.get("type"))
//
//                    }
//                }.addOnFailureListener {
//                    Log.e(SplashFragment::class.java.simpleName,"exception "+it.message)
//                }
//
//
//            }
//        }
    }

    private fun setSetupBottomNavAndNavHost() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        mNavController = navHostFragment.navController

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(mNavController)

        bottomNavigationView.menu.forEach {             TooltipCompat.setTooltipText(findViewById(it.itemId), null) }


    }
    private fun parentScreenList() {
        parentScreen.add(R.id.homeFragment)
        parentScreen.add(R.id.chatFragment)
//        parentScreen.add(R.id.comingSoonFragment)
        parentScreen.add(R.id.bookingListingFragment)
        parentScreen.add(R.id.profileMenuFragment)
    }

}
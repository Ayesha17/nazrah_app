package com.nazrah.nazrahapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
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
        navigationDestinationListener()

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
        val radius = resources.getDimension( R.dimen.dp24)


        val bottomBarBackground = binding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .build()

    }

    private fun navigationDestinationListener() {
        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            checkBottomNavigation(destination.id)
        }
    }

    private fun checkBottomNavigation(id: Int) {
        if (parentScreen.contains(id)) {
            binding.coordinatorLayout.visibility = View.VISIBLE
        } else
            binding.coordinatorLayout.visibility = View.GONE

    }
    private fun parentScreenList() {
        parentScreen.add(R.id.homeFragment)
        parentScreen.add(R.id.profileFragment)
    }

}
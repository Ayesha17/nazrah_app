package com.nazrah.nazrahapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.nazrah.nazrahapp.databinding.ActivityMainBinding
import com.nazrah.nazrahapp.fragments.splash.SplashFragment
import com.nazrah.nazrahapp.utils.Constants
import com.nazrah.nazrahapp.utils.FirebaseUtils
import com.nazrah.nazrahapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            delay(5000)
            if (FirebaseUtils.firebaseAuth.currentUser != null) {
                val db = Firebase.firestore(Firebase.app)
                db.collection(Constants.USERS).document(FirebaseUtils.firebaseAuth.currentUser?.uid!!).get().addOnCompleteListener {
                    if (it.isSuccessful){


                        if (it.result.get("type")==1L){
                            binding.navigationId.createClass.setVisible(true)
                            binding.navigationId.joinClassID.setVisible(false)
                        }
                        else if (it.result.get("type")==2L){
                            binding.navigationId.createClass.setVisible(false)
                            binding.navigationId.joinClassID.setVisible(true)
                        }
                        else
                        {
                            binding.navigationId.createClass.setVisible(false)
                            binding.navigationId.joinClassID.setVisible(false)
                        }
                        Log.e(SplashFragment::class.java.simpleName,"Constants.USERS type "+ it.result.get("type"))

                    }
                }.addOnFailureListener {
                    Log.e(SplashFragment::class.java.simpleName,"exception "+it.message)
                }


            }
        }
    }
}
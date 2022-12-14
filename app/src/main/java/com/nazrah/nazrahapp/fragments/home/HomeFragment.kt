package com.nazrah.nazrahapp.fragments.home

import android.view.View
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.firebase.auth.FirebaseAuth
import com.nazrah.nazrahapp.R
import com.nazrah.nazrahapp.base.BaseFragment
import com.nazrah.nazrahapp.databinding.FragmentHomeBinding
import com.nazrah.nazrahapp.utils.toastMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun getFragmentLayout() = R.layout.fragment_home

    override fun getViewBinding() {
        mBinding = binding as FragmentHomeBinding
    }

    override fun getViewModel() {
    }

    override fun observe() {
        auth = FirebaseAuth.getInstance()

    }

    override fun setLiveDataValues() {

    }

    override fun init() {


//        mBinding.createClassBtn.setOnClickListener {
//
//
//
//
//
//            val params: MutableMap<String, Any> = HashMap()
//
//            params["code"]=sixDigitsCode()
//            params["title"]=mBinding.editTextName.text.toString()
//            params["passing_percentage"]=mBinding.editTextpassingpercent.text.toString()
//            params["ducumentid"]=FirebaseUtils.firebaseAuth.currentUser?.uid.toString()
//            params["participants"]=arrayListOf<String>()
//
//
//            val db=FirestoreSource(Firebase.firestore(Firebase.app))
//            db.createClass(params, ::handleResult)
//            Log.e(HomeFragment::class.java.simpleName,"code is "+ sixDigitsCode())
//        }



    }

    fun handleResult(result:String){

        context?.toastMessage("data result "+result)

    }

    override fun setListeners() {
        mBinding.apply {

        }
    }

    override fun setLanguageData() {

    }

    override fun onClick(view: View?) {
    }

}
package com.example.loginmvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.loginmvvm.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val RC_SIGN_IN = 21

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupAuth()
    }

    private fun setupAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if(user == null){
                startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                    .setIsSmartLockEnabled(false)
                    .setAvailableProviders(
                        Arrays.asList(
                            AuthUI.IdpConfig.EmailBuilder().build(),
                            AuthUI.IdpConfig.GoogleBuilder().build())
                    )
                    .build(), RC_SIGN_IN)

            }
        }

    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth?.addAuthStateListener(mAuthListener)

        mBinding.tvName.text = FirebaseAuth.getInstance().currentUser?.displayName
        mBinding.tvEmail.text = FirebaseAuth.getInstance().currentUser?.email

        mBinding.btnLogout.setOnClickListener { singOut() }

    }

    private fun singOut() {
        this?.let {
            AuthUI.getInstance().signOut(it)
                .addOnCompleteListener{
                    Toast.makeText(this,"Hasta pronto...",Toast.LENGTH_SHORT).show()
                }
        }
    }


    override fun onPause() {
        super.onPause()
        mFirebaseAuth?.removeAuthStateListener (mAuthListener)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==RC_SIGN_IN){
            if (resultCode== RESULT_OK){
                Toast.makeText(this,"Bienvenido...", Toast.LENGTH_SHORT).show()
            }else{
                if(IdpResponse.fromResultIntent(data)==null){
                    finish()
                }
            }
        }

    }


}
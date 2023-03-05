package com.example.firebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.firebaseproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onStart() {

        val currentUser= auth.currentUser
        if (currentUser!=null){

            val intent= Intent(this@MainActivity,FeedActivity::class.java)
            finish()
            startActivity(intent)

        }

        super.onStart()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

    fun signInButton(view: View) {

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.equals("")|| password.equals("")){

            Toast.makeText(this@MainActivity,"E mail ve şifre boş bırakılamaz",Toast.LENGTH_LONG).show()

        }else{

            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {

                val intent= Intent(this@MainActivity,FeedActivity::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener {

                Toast.makeText(this@MainActivity,"Hatalı e posta veya şifre.",Toast.LENGTH_LONG).show()

            }

        }

    }

    fun signUpButton(view: View) {

        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.equals("")|| password.equals("")){

            Toast.makeText(this,"E mail ve şifre boş bırakılamaz",Toast.LENGTH_LONG).show()

        }else{

            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                val intent = Intent(this@MainActivity,FeedActivity::class.java)
                finish()
                startActivity(intent)

            }.addOnFailureListener {

                Toast.makeText(this@MainActivity,"Hata! böyle bir hesap zaten var veya başka bir şey. Parola da en az 6 hane olmalı. Gözden geçirin",Toast.LENGTH_LONG).show()

            }

        }


    }

}
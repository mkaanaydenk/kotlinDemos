package com.example.firebaseproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.firebaseproject.databinding.ActivityFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_post) {

            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)

        } else if (item.itemId == R.id.signout) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Emin misin?")
            alertDialog.setMessage("Çıkış yapmak istediğinden emin misin ?")
            alertDialog.setPositiveButton("Evet") { dialogInterface, which ->

                auth.signOut()
                val intent = Intent(this@FeedActivity, MainActivity::class.java)
                finish()
                startActivity(intent)

            }
            alertDialog.setNegativeButton("Vazgeç") { dialogInterface, which ->

            }.show()

        }

        return super.onOptionsItemSelected(item)
    }

}
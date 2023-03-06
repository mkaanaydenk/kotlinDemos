package com.example.firebaseproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseproject.model.Post
import com.example.firebaseproject.R
import com.example.firebaseproject.adapter.PostAdapter
import com.example.firebaseproject.databinding.ActivityFeedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var postList: ArrayList<Post>
    private lateinit var postAdapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        db = Firebase.firestore
        postList = ArrayList()

        getData()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        postAdapter = PostAdapter(postList)
        binding.recyclerView.adapter = postAdapter

    }

    private fun getData() {

        db.collection("Posts").orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    Toast.makeText(
                        this@FeedActivity,
                        "İlginç bir hata oluştu. Ancak no problem",
                        Toast.LENGTH_LONG
                    ).show()

                } else {

                    if (value != null) {
                        if (!value.isEmpty) {

                            val documents = value.documents

                            postList.clear()

                            for (document in documents) {

                                val userEmail = document.get("userEmail") as String
                                val comment = document.get("comment") as String
                                val downloadUrl = document.get("downloadUrl") as String

                                val post = Post(userEmail, comment, downloadUrl)

                                postList.add(post)

                            }
                            postAdapter.notifyDataSetChanged()

                        }
                    }

                }

            }


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
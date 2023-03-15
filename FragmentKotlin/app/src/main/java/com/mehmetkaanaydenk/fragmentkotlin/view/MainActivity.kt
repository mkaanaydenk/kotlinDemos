package com.mehmetkaanaydenk.fragmentkotlin.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.mehmetkaanaydenk.fragmentkotlin.R
import com.mehmetkaanaydenk.fragmentkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_item) {

            val action = MainFragmentDirections.actionMainFragmentToDetailsFragment("new",0)
            Navigation.findNavController(this, R.id.fragment).navigate(action)

        }
        return super.onOptionsItemSelected(item)
    }

}
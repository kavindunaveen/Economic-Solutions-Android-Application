package com.example.crudmyapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    // Variable declarations
    private var recyclerView: RecyclerView? = null
    private var mainAdapter: MainAdapter? = null
    private var floatingActionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        recyclerView = findViewById<View>(R.id.rv) as RecyclerView
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        // Configure FirebaseRecyclerOptions
        val options = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(
                FirebaseDatabase.getInstance().reference.child("Items"),
                MainModel::class.java
            )
            .build()

        // Initialize the MainAdapter.

        mainAdapter = MainAdapter(options)
        recyclerView!!.adapter = mainAdapter
        floatingActionButton = findViewById<View>(R.id.floatingActionButton) as FloatingActionButton
        floatingActionButton!!.setOnClickListener {
            startActivity(
                Intent(
                    applicationContext, AddActivity::class.java
                )
            )
        }
    }

    // Method called when the activity is started.
    override fun onStart() {
        super.onStart()
        mainAdapter!!.startListening()
    }

    // Method called when the activity is stopped.
    override fun onStop() {
        super.onStop()
        mainAdapter!!.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                // Call the txtSearch method
                txtSearch(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                txtSearch(query)
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun txtSearch(str: String) {
        val options = FirebaseRecyclerOptions.Builder<MainModel>()
            .setQuery(
                FirebaseDatabase.getInstance().getReference().child("Items")
                    .orderByChild("itemName")
                    .startAt(str)
                    .endAt(str + "\uf8ff"), MainModel::class.java
            )
            .build()

        mainAdapter = MainAdapter(options)
        mainAdapter!!.startListening()
        recyclerView!!.adapter = mainAdapter
    }
}

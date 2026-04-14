package com.example.zodiac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    var horoscopeList: List<Horoscope> = Horoscope.horoscopeList

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)

        val adapter = HoroscopeAdapter(horoscopeList) { position ->
            val horoscope = horoscopeList[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("HOROSCOPE_ID", horoscope.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main, menu)

        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("ZODIAC", "Buscando: $query")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i("ZODIAC", "Escribiendo: $newText")
                return true
            }
        })

        return true
    }
}
package com.example.zodiac.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.zodiac.R
import com.example.zodiac.data.Horoscope
import com.example.zodiac.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class DetailActivity : AppCompatActivity() {
    lateinit var horoscope: Horoscope

    lateinit var signImageView: ImageView
    lateinit var nameTextView: TextView
    lateinit var datesTextView: TextView
    lateinit var horoscopeLuckTextView: TextView

    lateinit var session: SessionManager
    lateinit var favoriteMenuItem: MenuItem

    var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        session = SessionManager(this)

        signImageView = findViewById(R.id.signImageView)
        nameTextView = findViewById(R.id.nameTextView)
        datesTextView = findViewById(R.id.datesTextView)
        horoscopeLuckTextView = findViewById(R.id.horoscopeLuckTextView)

        val id = intent.getStringExtra("HOROSCOPE_ID")!!

        horoscope = Horoscope.getById(id)!!

        isFavorite = session.isFavoriteHoroscope(id)

        nameTextView.setText(horoscope.name)
        datesTextView.setText(horoscope.dates)
        signImageView.setImageResource(horoscope.image)

        supportActionBar?.setTitle(horoscope.name)
        supportActionBar?.setSubtitle(horoscope.dates)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getHoroscopeLuck()
    }

    // Que menu se quiere cargar en el ActionBar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_detail, menu)
        favoriteMenuItem = menu.findItem(R.id.menu_favorite)
        setFavoriteIcon()
        return true
    }

    // Que hacemos cuando se pulse una opción del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_favorite -> {
                setFavorite()
                true
            }
            R.id.menu_share -> {
                share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFavoriteIcon () {
        if (isFavorite) {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite_selected)
        } else {
            favoriteMenuItem.setIcon(R.drawable.ic_favorite)
        }
    }

    fun setFavorite() {
        if (isFavorite) {
            session.setFavoriteHoroscope("")
        } else {
            session.setFavoriteHoroscope(horoscope.id)
        }
        isFavorite = !isFavorite
        setFavoriteIcon()
    }

    fun share() {
        val sendIntent = Intent()
        sendIntent.setAction(Intent.ACTION_SEND)
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        sendIntent.setType("text/plain")

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun getHoroscopeLuck() {
        CoroutineScope(Dispatchers.IO).launch {
            val urlGetRequest = URL("https://freehoroscopeapi.com/api/v1/get-horoscope/daily?sign=${horoscope.id}")

            // HTTP Connexion
            val apiConnexion = urlGetRequest.openConnection() as HttpsURLConnection

            // Method
            apiConnexion.setRequestMethod("GET")

            try {
                // Response code
                val responseCode = apiConnexion.getResponseCode()

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    val `in` = BufferedReader(InputStreamReader(apiConnexion.getInputStream()))
                    val response = StringBuffer()
                    var inputLine: String? = null

                    while ((`in`.readLine().also { inputLine = it }) != null) {
                        response.append(inputLine)
                    }
                    `in`.close()

                    // Return response
                    val result = JSONObject(response.toString()).getJSONObject("data").getString("horoscope")
                    horoscopeLuckTextView.text = result
                } else {
                    // Hubo algun error inesperado
                    horoscopeLuckTextView.text = "Hubo algun error inesperado"
                }
            } catch (e: Exception) {
                // Hubo algun error inesperado
                horoscopeLuckTextView.text = "Hubo algun error inesperado"
            } finally {
                Log.e("Disconnection", "e.toString()")
                apiConnexion.disconnect()
            }
        }
    }
}
package com.example.opsc_ice_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var adviceTextView: TextView
    private lateinit var resetButton: Button
    private lateinit var saveButton: Button
    private lateinit var searchButton: Button
    private lateinit var viewRatingsButton: Button
    private lateinit var ratingBar: RatingBar
    private var currentAdvice: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adviceTextView = findViewById(R.id.Advice)
        resetButton = findViewById(R.id.resetBtn)
        saveButton = findViewById(R.id.saveBtn)
        searchButton = findViewById(R.id.searchBtn)
        viewRatingsButton = findViewById(R.id.viewRatingsBtn)
        ratingBar = findViewById(R.id.ratingBar)

        fetchRandomAdvice()

        resetButton.setOnClickListener {
            fetchRandomAdvice()
        }

        saveButton.setOnClickListener {
            val rating = ratingBar.rating
            if (currentAdvice.isNotEmpty() && rating > 0) {
                RatingRepository.ratings.add(Rating(currentAdvice, rating))
                Toast.makeText(this, "Rating saved!", Toast.LENGTH_SHORT).show()
                fetchRandomAdvice() // Fetch new advice after saving the current one
            } else {
                Toast.makeText(this, "Please provide a valid rating.", Toast.LENGTH_SHORT).show()
            }
        }

        searchButton.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        viewRatingsButton.setOnClickListener {
            val intent = Intent(this, ViewRatingsPage::class.java)
            startActivity(intent)
        }
    }

    private fun fetchRandomAdvice() {
        val url = "https://api.adviceslip.com/advice"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { json ->
                    val slipResponse = Gson().fromJson(json, SlipResponse::class.java)
                    runOnUiThread {
                        currentAdvice = slipResponse.slip.advice
                        adviceTextView.text = currentAdvice
                        ratingBar.rating = 0f // Reset the rating bar for new advice
                    }
                }
            }
        })
    }
}

data class SlipResponse(val slip: Slip)
data class Slip(val id: Int, val advice: String)

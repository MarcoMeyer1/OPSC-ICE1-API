package com.example.opsc_ice_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class SearchPage : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var searchResultLabel: TextView
    private lateinit var resultView: RecyclerView
    private lateinit var adviceAdapter: AdviceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_page)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        searchResultLabel = findViewById(R.id.searchResultLabel)
        resultView = findViewById(R.id.resultView)

        adviceAdapter = AdviceAdapter(emptyList())
        resultView.layoutManager = LinearLayoutManager(this)
        resultView.adapter = adviceAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            searchAdvice(query)
        }
    }

    private fun searchAdvice(query: String) {
        val url = "https://api.adviceslip.com/advice/search/$query"
        val request = Request.Builder().url(url).build()

        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { json ->
                    val searchResponse = Gson().fromJson(json, SearchResponse::class.java)
                    runOnUiThread {
                        if (searchResponse.slips.isNotEmpty()) {
                            adviceAdapter.updateAdviceList(searchResponse.slips)
                            searchResultLabel.text = "Results found: ${searchResponse.total_results}"
                        } else {
                            adviceAdapter.updateAdviceList(emptyList())
                            searchResultLabel.text = "No advice found."
                        }
                    }
                }
            }
        })
    }
}

data class SearchResponse(val total_results: String, val slips: List<Slip>)


class AdviceAdapter(private var adviceList: List<Slip>) : RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return AdviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.bind(adviceList[position])
    }

    override fun getItemCount(): Int = adviceList.size

    fun updateAdviceList(newAdviceList: List<Slip>) {
        adviceList = newAdviceList
        notifyDataSetChanged()
    }

    class AdviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adviceTextView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(advice: Slip) {
            adviceTextView.text = advice.advice
        }
    }
}


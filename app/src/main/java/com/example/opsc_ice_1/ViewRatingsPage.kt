package com.example.opsc_ice_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewRatingsPage : AppCompatActivity() {

    private lateinit var ratingsRecyclerView: RecyclerView
    private lateinit var ratingsAdapter: RatingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ratings)

        ratingsRecyclerView = findViewById(R.id.ratingsRecyclerView)
        ratingsAdapter = RatingsAdapter(RatingRepository.ratings)
        ratingsRecyclerView.layoutManager = LinearLayoutManager(this)
        ratingsRecyclerView.adapter = ratingsAdapter
    }
}

class RatingsAdapter(private val ratingsList: List<Rating>) : RecyclerView.Adapter<RatingsAdapter.RatingsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return RatingsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatingsViewHolder, position: Int) {
        holder.bind(ratingsList[position])
    }

    override fun getItemCount(): Int = ratingsList.size

    class RatingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adviceTextView: TextView = itemView.findViewById(android.R.id.text1)
        private val ratingTextView: TextView = itemView.findViewById(android.R.id.text2)

        fun bind(rating: Rating) {
            adviceTextView.text = rating.advice
            ratingTextView.text = "Rating: ${rating.rating}"
        }
    }
}

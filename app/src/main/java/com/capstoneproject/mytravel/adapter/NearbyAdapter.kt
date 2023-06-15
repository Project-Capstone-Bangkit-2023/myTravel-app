package com.capstoneproject.mytravel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstoneproject.mytravel.BuildConfig
import com.capstoneproject.mytravel.R
import com.capstoneproject.mytravel.retrofit.WeatherResponse
import com.capstoneproject.mytravel.retrofit.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DecimalFormat

class NearbyAdapter(private val context: Context, private val listNearby: List<Nearby>) : RecyclerView.Adapter<NearbyAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val tvWeather: TextView = itemView.findViewById(R.id.tvWeather)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_nearby, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = listNearby.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, name, category, photo, city, rating, price, desc, lat, lon, distance) = listNearby[position]

        fun getTemperature(lat: Double, lon: Double){
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val weatherService = retrofit.create(WeatherService::class.java)

            val call = weatherService.getCurrentWeather(lat, lon, BuildConfig.APP_ID)
            call.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(
                    call: Call<WeatherResponse>,
                    response: Response<WeatherResponse>
                ) {
                    if (response.isSuccessful) {
                        val weatherResponse = response.body()
                        weatherResponse?.let {
                            val temperatureKelvin = it.main.temp.toString()
                            val temperatureCelsius = temperatureKelvin.toDouble() - 273.0
                            val temperature = DecimalFormat("##.#").format(temperatureCelsius)
                            holder.tvWeather.text = context.getString(R.string.temperature, temperature)
                        }
                    } else {
                        holder.tvWeather.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }
            })
        }

        val photoUrl = "https://storage.googleapis.com/mytravel_bucket/places/$photo"
        val formatDistance = String.format("%.1f", distance)

        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .into(holder.imgPhoto)
        holder.tvName.text = name
        holder.tvAddress.text = city
        holder.tvDistance.text = context.getString(R.string.km, formatDistance)
        holder.tvWeather.text = "0.0"
        if (lat != null && lon != null) {
            getTemperature(lat,lon)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNearby[holder.adapterPosition])
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Nearby)
    }
}
package com.example.myapplication

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.myapplication.data.Place
import com.example.myapplication.data.Tags
import com.google.android.gms.maps.model.LatLng
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SearchNearbyAPI(
    _categories: ArrayList<String>,
    _key: String,
    _radius: Int,
    _latLng: LatLng) {
    private var categories = _categories
    private var key = _key
    private var radius = _radius
    private var latLng = _latLng

    fun requestSearch(category: String): ArrayList<Place>{
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                "?location=${latLng.latitude},${latLng.longitude}" +
                "&radius=${radius}" +
                "&type=${category}" +
                "&key=${key}"

        val request = Request.Builder().url(url).build()

        Log.d(Tags.MAP, "Searching for $category")
        val response = OkHttpClient().newCall(request).execute().body()?.string()
        val jsonObject =
            response?.let { JSONObject(it) } // This will make the json below as an object for you

        if (jsonObject != null) {
            if( jsonObject.get("status").toString() == "OK") {
                Log.d(Tags.MAP, "$category found!")
                val placesJSON = jsonObject.getJSONArray("results")
                val placesArray = arrayListOf<Place>()

                for (i in 0 until placesJSON.length()) {
                    val placeJSON = placesJSON.getJSONObject(i)

                    var placeName = placeJSON.get("name").toString()
                    if(placeName.length > 23){
                        placeName = placeName.take(20).plus("...")
                    }

                    var placeAddress = placeJSON.get("vicinity").toString()
                    placeAddress = formatVicinity(placeAddress)

                    val place = Place(placeName, placeAddress)
                    placesArray.add(place)
                }
                return placesArray
            } else {
                Log.d(Tags.MAP, "No such places found nearby")
                return arrayListOf()
            }
        } else {
            Log.d(Tags.MAP, "JSON object is null")
            return arrayListOf()
        }
    }

    private fun formatVicinity(vicinity: String): String{
        Log.d(Tags.MAP, vicinity)
        val lastCommaIndex = vicinity.lastIndexOf(',')
        var formattedVicinity = vicinity
        if(lastCommaIndex != -1){
            formattedVicinity = vicinity.substring(0, lastCommaIndex)
        }
        if(formattedVicinity.length > 23){
            formattedVicinity = formattedVicinity.take(20).plus("...")
        }
        Log.d(Tags.MAP, formattedVicinity)
        return formattedVicinity
    }

    fun getLocation(context: Context,  latLng: LatLng): String{
        val geocoder = Geocoder(context)
        var location = ""
        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1, object: Geocoder.GeocodeListener {
            override fun onGeocode(addresses: MutableList<Address>) {
                Log.d(Tags.RESULTS, addresses[0].toString())
                if(addresses[0].thoroughfare != null)
                    location = addresses[0].thoroughfare.toString() +
                        ", " + addresses[0].locality.toString()
                else{
                    val nLatLng = LatLng(latLng.latitude, latLng.longitude + 0.0001)
                    location = getLocation(context, nLatLng)
                }
            }
            override fun onError(errorMessage: String?) {
                super.onError(errorMessage)
                location = "Nieznany błąd"
            }
        })
        while(location.isEmpty())
            continue
        return location
    }
    fun setRadius(radius: Int){
        this.radius = radius
    }
    fun setLocation(latLng: LatLng){
        this.latLng = latLng
    }
    fun setCategory(categories: ArrayList<String>){
        this.categories = categories
    }
}
package com.example.myapplication.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.data.Request
import com.example.myapplication.data.Tags
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.myapplication.results.ResultsActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var locationPermissionGranted = false;

    private val REQUEST_LOCATION_CODE = 101
    private val DEFAULT_ZOOM = 19
    private val DEFAULT_LOCATION = LatLng(-33.8523341, 151.2106085)

    private var lastKnownLocation: Location? = null
    private var markedLocation: Marker? = null
    private var markedLatLng: LatLng? = null

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
            if(isGranted){
                locationPermissionGranted = true
                updateLocationUI()
                getDeviceLocation()
            } else{
                // permission not granted
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        supportMapFragment.getMapAsync(this)

        init()
        return binding.root
    }

    private fun init() {
        // Initialize the SDK
        Places.initialize(requireActivity().applicationContext, getString(R.string.api_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(requireContext())

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.LAT_LNG, Place.Field.ADDRESS))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                Log.d(Tags.MAP, "Place clicked: ${place}")

                val latLng = place.latLng
                val address = place.address
                if (latLng != null && address != null) {
                    moveCamera(latLng, DEFAULT_ZOOM.toFloat())
                    putMarker(latLng, address)

                    val button = requireActivity().findViewById<Button>(R.id.checkButton)
                    if(button.visibility == View.GONE)
                        button.visibility = View.VISIBLE

                } else {
                    Log.d(Tags.MAP, "Place not found")
                }
            }

            override fun onError(status: Status) {
                Log.i(Tags.MAP, "An error occurred: $status")
            }
        })

        val button = binding.checkButton
        button.setOnClickListener {
            if(markedLatLng != null){
                val categories = (activity as MapsActivity).selectedCategories
                val radius = (activity as MapsActivity).selectedRadius
                Log.d(Tags.MAP, categories.toString())
                val intent = Intent(requireContext(), ResultsActivity::class.java)
                val request = Request(
                    getString(R.string.api_key),
                    radius,
                    markedLatLng!!.latitude,
                    markedLatLng!!.longitude,
                    categories)
                intent.putExtra("REQUEST", request)
                startActivity(intent)
            }
        }
    }

    // Ask for location permission
    private fun enableMyLocation() {
        Log.d(Tags.MAP, "Ask for location permission")
        if (ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        }
        else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


    // If user allowed sharing localization, centers camera on device
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        Log.d(Tags.MAP, "Update Location UI")
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // Get device location, move camera if found.
    private fun getDeviceLocation() {
        Log.d(Tags.MAP, "Get device location")
        try {
            Log.d(Tags.MAP, "Checking if permission is granted")
            if(locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) {task ->
                    if(task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val latLng = LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                            moveCamera(latLng, DEFAULT_ZOOM.toFloat())
                        }
                    } else {
                        Log.d(Tags.MAP, "Current location is null. Using defaults.")
                        Log.e(Tags.MAP, "Exception: %s", task.exception)
                        moveCamera(DEFAULT_LOCATION, DEFAULT_ZOOM.toFloat())
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // Move camera to specified location in UI thread
    private fun moveCamera(latLng: LatLng, zoom: Float) {
        requireActivity().runOnUiThread {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
        }
    }

    // Put marker in specified location in UI thread
    private fun putMarker(latLng: LatLng, title: String) {
        markedLocation?.remove()
        val markerOptions = MarkerOptions().position(latLng).title(title)
        requireActivity().runOnUiThread {
            val marker = mMap.addMarker(markerOptions)
            markedLocation = marker
            markedLatLng = latLng
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setPadding(0, 160, 0, 0)

        mMap.setOnMapClickListener {
            Log.d(Tags.MAP, it.toString())
            putMarker(it, "Selected Location")
            val button = requireActivity().findViewById<Button>(R.id.checkButton)
            if(button.visibility == View.GONE)
                button.visibility = View.VISIBLE
        }

        enableMyLocation()
        updateLocationUI()
        getDeviceLocation()
    }

}
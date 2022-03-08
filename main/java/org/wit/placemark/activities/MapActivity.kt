package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.placemark.R
//import org.wit.placemark.activities.databinding.ActivityMapBinding
import org.wit.placemark.databinding.ActivityMapBinding
import timber.log.Timber.i
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        i("in map 1")
       super.onCreate(savedInstanceState)
         i("in map 2")
        binding = ActivityMapBinding.inflate(layoutInflater)
        i("in map 3")
        setContentView(binding.root)
        i("in map 4")
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        i("in map 5")
        mapFragment.getMapAsync(this)
        i("in map 6")
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
        i("in map 7")
        mMap = googleMap
        i("in map 8")
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        i("in map 9")
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        i("in map 10")
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        i("in map 11")
    }
}
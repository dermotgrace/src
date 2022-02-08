package org.wit.placemark.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacemarkBinding
    val placemarks = ArrayList<PlacemarkModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.plant(Timber.DebugTree())

        i("Placemark Activity started...")

        binding.btnAdd.setOnClickListener() {
            var placemark = PlacemarkModel()
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.placemarkDescription.text.toString()

            if (placemark.title.isNotEmpty() && placemark.description.isNotEmpty()) {
                i("add Button Pressed: $placemarks")
                placemarks.add(placemark)
            }
            else {
                Snackbar
                    .make(it,"Please fill all fields", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }
}
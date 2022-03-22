package org.wit.placemark.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.wit.placemark.R
import org.wit.placemark.databinding.ActivityPlacemarkBinding
import org.wit.placemark.helpers.showImagePicker
import org.wit.placemark.main.MainApp
import org.wit.placemark.models.Location
import org.wit.placemark.models.PlacemarkModel
import timber.log.Timber.i

class PlacemarkActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlacemarkBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    var placemark = PlacemarkModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacemarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)

        app = application as MainApp

        if (intent.hasExtra("placemark_edit")) {
            placemark = intent.extras?.getParcelable("placemark_edit")!!
            binding.placemarkTitle.setText(placemark.title)
            binding.placemarkDescription.setText(placemark.description)
            binding.chooseImage.setText(getString(R.string.button_changeImage))
            binding.btnAdd.setText(getString(R.string.button_savePlacemark))
            Picasso.get()
                .load(placemark.image)
                .into(binding.placemarkImage)
        } else {
            binding.btnAdd.setText(getString(R.string.button_addPlacemark))
        }


        i("Placemark Activity started...")
        binding.btnAdd.setOnClickListener() {
            placemark.title = binding.placemarkTitle.text.toString()
            placemark.description = binding.placemarkDescription.text.toString()
            if (placemark.title.isNotEmpty()) {
                if (!intent.hasExtra("placemark_edit")) {
                    app!!.placemarks.create(placemark.copy())
                } else {
                    app!!.placemarks.update(placemark.copy())
                }
                setResult(RESULT_OK)
                finish()
            }
            else {
                Snackbar.make(it,getString(R.string.placemark_EnterTitle), Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher,this)
        }

        binding.placemarkLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (placemark.zoom != 0f) {
                location.lat =  placemark.lat
                location.lng = placemark.lng
                location.zoom = placemark.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }

        registerImagePickerCallback()
        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.placemarks.delete(placemark)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            placemark.image = image

                            Picasso.get()
                                .load(placemark.image)
                                .into(binding.placemarkImage)
                            binding.chooseImage.setText(R.string.change_placemark_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        i("Register map callback")
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            placemark.lat = location.lat
                            placemark.lng = location.lng
                            placemark.zoom = location.zoom
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }
}
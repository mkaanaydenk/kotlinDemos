package com.example.roomdatabase.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Picture
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.roomdatabase.R
import com.example.roomdatabase.databinding.ActivityDetailsBinding
import com.example.roomdatabase.roomdb.PictureDao
import com.example.roomdatabase.roomdb.PictureDatabase
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null
    private lateinit var db: PictureDatabase
    private lateinit var pictureDao: PictureDao
    val compositeDisposable = CompositeDisposable()
    var selectedPicture: com.example.roomdatabase.model.Picture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

        db = Room.databaseBuilder(applicationContext, PictureDatabase::class.java, "Pictures")
            //.allowMainThreadQueries()
            .build()
        pictureDao = db.pictureDao()

        val info = intent.getStringExtra("info")

        if (info=="old") {

            selectedPicture =
                intent.getSerializableExtra("selectedPicture") as com.example.roomdatabase.model.Picture

            selectedPicture?.let {

                binding.nameText.setText(it.name)
                binding.descriptionText.setText(it.descriptions)

                val byteArray = it.image
                val bitmap = byteArray?.let { it1 ->
                    BitmapFactory.decodeByteArray(
                        byteArray, 0,
                        it1.size
                    )
                }
                binding.imageView.setImageBitmap(bitmap)

                binding.button.visibility = View.INVISIBLE

            }


        }

    }

    fun selectImage(view: View) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                ) {

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give permission", View.OnClickListener {
                            //req permission
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()

                } else {
                    //req permission
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }

            } else {
                //go to gallery
                val intentToGallery: Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        } else {

            if (ContextCompat.checkSelfPermission(
                    this@DetailsActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@DetailsActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {

                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Give permission", View.OnClickListener {
                            //req permission
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()

                } else {
                    //req permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            } else {
                //go to gallery
                val intentToGallery: Intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }

    }

    fun save(view: View) {

        val name = binding.nameText.text.toString()
        val description = binding.descriptionText.text.toString()

        val outputStream = ByteArrayOutputStream()
        selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
        val byteArray = outputStream.toByteArray()

        val picture: com.example.roomdatabase.model.Picture =
            com.example.roomdatabase.model.Picture(name, description, byteArray)

        compositeDisposable.add(

            pictureDao.insert(picture)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlerResponse)
        )

    }

    private fun handlerResponse() {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)

    }

    fun registerLauncher() {

        activityResultLauncher = registerForActivityResult(StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {

                val resultIntent = result.data

                if (resultIntent != null) {

                    val uri = resultIntent.data

                    if (Build.VERSION.SDK_INT >= 28) {

                        if (uri != null) {
                            val source = ImageDecoder.createSource(contentResolver, uri)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedBitmap)
                        }

                    } else {

                        selectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        binding.imageView.setImageBitmap(selectedBitmap)

                    }

                }

            }

        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

                if (result) {

                    val intentToGallery: Intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                } else {

                    Toast.makeText(
                        this@DetailsActivity,
                        "permission needed for gallery!",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}
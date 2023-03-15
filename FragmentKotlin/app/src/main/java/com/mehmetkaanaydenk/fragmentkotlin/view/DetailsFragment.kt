package com.mehmetkaanaydenk.fragmentkotlin.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.mehmetkaanaydenk.fragmentkotlin.R
import com.mehmetkaanaydenk.fragmentkotlin.databinding.FragmentDetailsBinding
import com.mehmetkaanaydenk.fragmentkotlin.model.Art
import com.mehmetkaanaydenk.fragmentkotlin.roomdb.ArtDao
import com.mehmetkaanaydenk.fragmentkotlin.roomdb.ArtDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.ByteArrayOutputStream


class DetailsFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    var selectedPicture: Bitmap?= null
    private lateinit var db: ArtDatabase
    private lateinit var artDao: ArtDao
    val compositeDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        registerLauncher()
        db= Room.databaseBuilder(requireContext().applicationContext,ArtDatabase::class.java,"Arts").build()
        artDao= db.artDao()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageSelectC()
        save()

        arguments?.let {

            val info= DetailsFragmentArgs.fromBundle(it).info
            if (info.equals("new")){

                binding.nameText.setText("")
                binding.yearText.setText("")
                binding.button.visibility= View.VISIBLE

            }else{

                binding.button.visibility= View.INVISIBLE

                val selectedId= DetailsFragmentArgs.fromBundle(it).id
                compositeDisposable.add(artDao.getArtById(selectedId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponseOld)
                )


            }



        }

    }

    fun handleResponseOld(art : Art){

        binding.nameText.setText(art.artName)
        binding.yearText.setText(art.artYear)
        art.image?.let {

            val bitmap= BitmapFactory.decodeByteArray(it,0,it.size)
            binding.imageView.setImageBitmap(bitmap)

        }

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.add_item).setVisible(false)
    }

    fun save(){

        binding.button.setOnClickListener {

            var name = binding.nameText.text.toString()
            var year = binding.yearText.text.toString()

            if (selectedPicture!=null){

                val outputStream= ByteArrayOutputStream()
                selectedPicture!!.compress(Bitmap.CompressFormat.PNG,50,outputStream)
                val byteArray= outputStream.toByteArray()

                val art= Art(name,year,byteArray)

                compositeDisposable.add(artDao.insert(art)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)

                )


            }

        }

    }

    fun handleResponse(){

        val action= DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
        Navigation.findNavController(requireView()).navigate(action)

    }

    fun imageSelectC(){

    binding.imageView.setOnClickListener {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext().applicationContext,android.Manifest.permission.READ_MEDIA_IMAGES)!=PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_MEDIA_IMAGES)){

                    Snackbar.make(it,"Galeri için izin gerekir",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver",View.OnClickListener {
                        //req permission
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()

                }else{

                    //req permission
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }

            }else{

                //go to gallery
                val intentToGallery: Intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }


        }else{

            if (ContextCompat.checkSelfPermission(requireContext().applicationContext,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE)){

                    Snackbar.make(it,"Galeri için izin gerekir",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver",View.OnClickListener {
                        //req permission
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()

                }else{

                    //req permission
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }

            }else{

                //go to gallery
                val intentToGallery: Intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }

        }




    }


   }

    fun registerLauncher(){

        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

            if (result.resultCode==AppCompatActivity.RESULT_OK){

                val resultIntent= result.data
                if (resultIntent!=null){

                    val uri= resultIntent.data

                    if (uri!=null){

                        if (Build.VERSION.SDK_INT>=28){

                            val source = ImageDecoder.createSource(requireActivity().contentResolver,uri)
                            selectedPicture= ImageDecoder.decodeBitmap(source)
                            binding.imageView.setImageBitmap(selectedPicture)


                        }else{

                            selectedPicture= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,uri)
                            binding.imageView.setImageBitmap(selectedPicture)


                        }


                    }

                }

            }

        }

        permissionLauncher= registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->

            if (result){

                val intentToGallery= Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }else{

                Toast.makeText(requireContext().applicationContext,"Galeri için izin gerekir!",Toast.LENGTH_LONG).show()

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }

}
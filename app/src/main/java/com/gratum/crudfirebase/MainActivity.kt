package com.gratum.crudfirebase

import android.app.PendingIntent.getActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gratum.crudfirebase.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream


class MainActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

    private lateinit var uri:Uri

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { it ->
        if (it != null) {
            //Imagen seleccionada
            binding.imageView.setImageURI(it)
            uri = it
            Toast.makeText(this, "Imagen seleccionada", Toast.LENGTH_SHORT).show()
        } else {
            //No imagen
            Toast.makeText(this, "Imagen No seleccionada", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var productAdapter: ProductAdapter
    private lateinit var list: ArrayList<Product>

    private var selected: Product = Product()

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initElement()
        initViewModel()
        uploadImage()
    }

    private fun uploadImage() {
        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initElement() {

        binding.submit.setOnClickListener {
            create()
        }
        list = ArrayList()

        // Get list
        productViewModel.getList()

    }

    private fun initViewModel() {
        productViewModel.createLiveData.observe(this) {
            onCreate(it)
        }

        productViewModel.updateLiveData.observe(this) {
            onUpdate(it)
        }

        productViewModel.deleteLiveData.observe(this) {
            onDelete(it)
        }

        productViewModel.getListLiveData.observe(this) {
            onGetList(it)
        }
    }

    private fun onCreate(it: Boolean) {
        if (it) {
            productViewModel.getList()
            resetText()
        }
    }

    private fun onUpdate(it: Boolean) {
        if (it) {
            productViewModel.getList()
            resetText()
        }
    }

    private fun onDelete(it: Boolean) {
        if (it) {
            productViewModel.getList()
            resetText()
        }
    }

    private fun onGetList(it: List<Product>) {
        list = ArrayList()
        list.addAll(it)

        productAdapter = ProductAdapter(list, this)

        binding.rvList.adapter = productAdapter
        binding.rvList.layoutManager = LinearLayoutManager(baseContext)

        productAdapter.notifyDataSetChanged()
    }

    //Crear document
    private fun create() {

        val urlFirebase = productViewModel.uploadImageToFirebase(uri).toString()


        val product = Product(
            selected.id,
            binding.name.text.toString(),
            binding.price.text.toString().toDouble(),
            binding.description.text.toString(),
            urlImage = urlFirebase
        )
        if (product.id != null) {
            productViewModel.update(product)
        } else {

            productViewModel.create(product)
        }
    }

    private fun resetText() {
        selected = Product()
        binding.name.text = null
        binding.price.text = null
        binding.description.text = null
    }

    override fun onClick(item: Product, position: Int) {
        selected = item
        binding.name.setText(selected.name)
        binding.price.setText(selected.price.toString())
        binding.description.setText(selected.description)
    }

    override fun onDelete(item: Product, position: Int) {
        productViewModel.delete(item.id!!)
    }
//
//    fun uploadImageToFirebase(uri: Uri): String? {
//        productViewModel.initStorageRef()
//        var uriRef: String? = null
//        storageRef.getReference("images").child(System.currentTimeMillis().toString())
//            .putFile(uri).addOnSuccessListener { task ->
//                task.metadata!!.reference!!.downloadUrl
//                    .addOnSuccessListener {it->
//                        uriRef = it.toString()
//                        Toast.makeText(this,"Se envio", Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener{
//                        uriRef = null
//                        Toast.makeText(this,"Fallo", Toast.LENGTH_SHORT).show()
//                    }
//            }
//        return uriRef
//    }
}
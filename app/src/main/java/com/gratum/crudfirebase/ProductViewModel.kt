package com.gratum.crudfirebase

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class ProductViewModel : ViewModel() {

    private var db = Firebase.firestore
    private val products = "products"
    private var storageRef = Firebase.storage

    fun initStorageRef() {
        storageRef = FirebaseStorage.getInstance()
    }

    val createLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val updateLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val getListLiveData: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    val deleteLiveData: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun create(product: Product) {
        val docRef = db.collection(products)

        docRef.add(product.toMap())
            .addOnSuccessListener {
                createLiveData.postValue(true)
            }
            .addOnFailureListener {
                Log.d("create", it.localizedMessage!!)
                createLiveData.postValue(false)
            }


    }

    fun update(product: Product) {
        val docRef = db.collection(products)
        docRef.document(product.id!!).update(product.toMap())
            .addOnSuccessListener {
                updateLiveData.postValue(true)
            }
            .addOnFailureListener {
                Log.d("update", it.localizedMessage!!)
                updateLiveData.postValue(false)
            }
    }

    fun delete(id: String) {
        val docRef = db.collection(products)
        docRef.document(id).delete()
            .addOnSuccessListener {
                deleteLiveData.postValue(true)
            }
            .addOnFailureListener {
                Log.d("delete", it.localizedMessage!!)
                deleteLiveData.postValue(false)
            }
    }

    fun getList() {
        val docRef = db.collection(products)
        docRef.get()
            .addOnSuccessListener {
                val products = ArrayList<Product>()
                for (item in it.documents) {
                    val product = Product()
                    product.id = item.id
                    product.name = item.data!!["name"] as String?
                    product.price = item.data!!["price"] as Double?
                    product.description = item.data!!["description"] as String?
//                product.create_date = item.data!!["create_date"] as Timestamp?
                    //    product.update_date = item.data!!["update_date"] as Timestamp?
                    products.add(product)
                }

                getListLiveData.postValue(products)
            }
            .addOnFailureListener {
                Log.d("get", it.localizedMessage!!)
                getListLiveData.postValue(null)
            }
    }

    fun uploadImageToFirebase(urlImage: Uri): Uri? {
        initStorageRef()

        var uriRef: String? = null
        var downloadUri: Uri? = null

        storageRef.getReference("images").child(System.currentTimeMillis().toString())
            .putFile(urlImage).addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener {task2->
                        downloadUri = task2
                    }
                    .addOnFailureListener {
                        uriRef = null
                        Log.d("Referencia Url rechazada", "$uriRef")
                    }


            }
        Log.d("referencia", "${uriRef.toString()}")
        return downloadUri
    }
}

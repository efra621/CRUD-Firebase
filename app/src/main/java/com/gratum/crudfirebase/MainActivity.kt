package com.gratum.crudfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.gratum.crudfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {

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
    }

    //Holaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

    private fun initElement() {
//        name = findViewById(R.id.name)
//        price = findViewById(R.id.price)
//        description = findViewById(R.id.description)
//        submit = findViewById(R.id.submit)
//        rvList = findViewById(R.id.rvList)

        binding.submit.setOnClickListener {
            create()
        }

        list = ArrayList()

//        submit.setOnClickListener {
//        }

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

    private fun create() {
        val product = Product(
            selected.id,
            binding.name.text.toString(),
            binding.price.text.toString().toDouble(),
            binding.description.text.toString(),
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

        //  selected.update_date = Timestamp.now()

        binding.name.setText(selected.name)
        binding.price.setText(selected.price.toString())
        binding.description.setText(selected.description)
    }

    override fun onDelete(item: Product, position: Int) {
        productViewModel.delete(item.id!!)
    }
}
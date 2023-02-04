package com.gratum.crudfirebase

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gratum.crudfirebase.databinding.ItemProductBinding

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemProductBinding.bind(itemView)

    fun bindItem(product: Product) {
        itemView.apply {
            binding.nameText.text = product.name
            binding.priceText.text = "${product.price} Bath"
            binding.descriptionText.text = product.description
        }
    }
}
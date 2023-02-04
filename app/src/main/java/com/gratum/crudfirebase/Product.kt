package com.gratum.crudfirebase

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import java.security.Timestamp

@IgnoreExtraProperties
data class Product(
    var id: String? = null,
    var name: String? = null,
    var price: Double? = null,
    var description: String? = null,

){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "price" to price,
            "description" to description,
        )
    }
}
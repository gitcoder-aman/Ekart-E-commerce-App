package com.tech.ekart.data.order

import com.tech.ekart.data.Address
import com.tech.ekart.data.CartProduct

data class Order(
    val orderStatus : String,
    val totalPrice : Float,
    val product : List<CartProduct>,
    val address : Address
)
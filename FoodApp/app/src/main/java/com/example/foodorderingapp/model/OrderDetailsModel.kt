package com.example.foodorderingapp.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.ArrayList

class OrderDetailsModel() : Serializable {
    var userUid: String? = null
    var userPerson: String? = null
    var foodName: MutableList<String>? = null
    var foodImage: MutableList<String>? = null
    var foodPrice: MutableList<String>? = null
    var foodQuantity: MutableList<Int>? = null
    var address: String? = null
    var totalPrice: String? = null
    var phone: String? = null
    var orderAccepted: Boolean = false
    var paymentReceived: Boolean = false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userPerson = parcel.readString()
        address = parcel.readString()
        totalPrice = parcel.readString()
        phone = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentReceived = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    constructor(
        userId: String,
        name: String,
        foodName: ArrayList<String>,
        foodImageUri: ArrayList<String>,
        foodPrice: ArrayList<String>,
        foodQuantities: ArrayList<Int>,
        address: String,
        totalAmount: String,
        phoneNum: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this() {
        this.userUid = userId
        this.userPerson = name
        this.foodName = foodName
        this.foodImage = foodImageUri
        this.foodPrice = foodPrice
        this.foodQuantity = foodQuantities
        this.address = address
        this.totalPrice = totalAmount
        this.phone = phoneNum
        this.orderAccepted = b
        this.paymentReceived = b1
        this.itemPushKey = itemPushKey
        this.currentTime = time
    }

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userPerson)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phone)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailsModel> {
        override fun createFromParcel(parcel: Parcel): OrderDetailsModel {
            return OrderDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailsModel?> {
            return arrayOfNulls(size)
        }
    }
}


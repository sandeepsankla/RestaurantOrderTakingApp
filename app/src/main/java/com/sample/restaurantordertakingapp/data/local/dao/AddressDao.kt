package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sample.restaurantordertakingapp.data.local.entity.AddressEntity


@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("SELECT * FROM addresses WHERE orderId = :orderId")
    suspend fun getAddressForOrder(orderId: String): AddressEntity?
}





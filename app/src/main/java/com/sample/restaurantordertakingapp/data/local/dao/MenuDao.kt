package com.sample.restaurantordertakingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.relation.CategoryWithItems
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.MenuWithCategories


@Dao
interface MenuDao {

    // -------- INSERT --------
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menu: MenuEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuItems(items: List<MenuItemEntity>)

    // -------- READ --------
    @Transaction
    @Query("SELECT * FROM menu WHERE id = 1")
    suspend fun getMenuWithCategories(): MenuWithCategories?

    @Transaction
    @Query("SELECT * FROM category")
    suspend fun getCategoriesWithItems(): List<CategoryWithItems>

    // -------- CLEAR --------
    @Query("DELETE FROM menu_item")
    suspend fun clearMenuItems()

    @Query("DELETE FROM category")
    suspend fun clearCategories()

    @Query("DELETE FROM menu")
    suspend fun clearMenu()
}

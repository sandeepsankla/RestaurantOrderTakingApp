package com.sample.restaurantordertakingapp.data.local

import com.sample.restaurantordertakingapp.data.local.dao.MenuDao
import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.MenuWithCategories
import javax.inject.Inject

class LocalMenuDataSource @Inject constructor(
    private val menuDao: MenuDao
) {
    /**
     * Read full menu with categories & items
     * Used by Repository to build Domain Menu
     */
    suspend fun getMenuOnce(): MenuWithCategories? {
        return menuDao.getMenuWithCategories()
    }

    /**
     * Save complete menu atomically
     */
    suspend fun insertFullMenu(
        menu: MenuEntity,
        categories: List<CategoryEntity>,
        items: List<MenuItemEntity>
    ) {
        menuDao.insertMenu(menu)
        menuDao.insertCategories(categories)
        menuDao.insertMenuItems(items)
    }

    /**
     * Clear all menu data (optional but useful)
     */
    suspend fun clearMenu() {
        menuDao.clearMenuItems()
        menuDao.clearCategories()
        menuDao.clearMenu()
    }
}

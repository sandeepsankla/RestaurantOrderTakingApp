package com.sample.restaurantordertakingapp.ui.theme.screen.mapper

import com.sample.restaurantordertakingapp.domain.model.Category
import com.sample.restaurantordertakingapp.domain.model.Menu
import com.sample.restaurantordertakingapp.domain.model.MenuItem
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.CategoryUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuItemUi
import com.sample.restaurantordertakingapp.ui.theme.screen.menu.MenuUi


fun Menu.toUi(): MenuUi {
    return MenuUi(
        categories = categories.map {it.toUi() })
}


fun Category.toUi(): CategoryUi =
    CategoryUi(
        id = id,
        name = name,
        items = items.map { it.toUi() }
    )
fun MenuItem.toUi(): MenuItemUi =
    MenuItemUi(
        id = id,
        name = name,
        fullPrice = fullPrice,
        halfPrice = halfPrice,
        imageUrl = imageUrl
    )

package com.sample.restaurantordertakingapp.data.mapper

import com.sample.restaurantordertakingapp.data.local.entity.CategoryEntity
import com.sample.restaurantordertakingapp.data.local.relation.CategoryWithItems
import com.sample.restaurantordertakingapp.data.local.entity.MenuEntity
import com.sample.restaurantordertakingapp.data.local.entity.MenuItemEntity
import com.sample.restaurantordertakingapp.data.local.relation.MenuWithCategories
import com.sample.restaurantordertakingapp.data.remote.firebase.model.MenuDocument
import com.sample.restaurantordertakingapp.domain.model.Menu
import com.sample.restaurantordertakingapp.domain.model.Category
import com.sample.restaurantordertakingapp.domain.model.MenuItem


fun Menu.menuToEntities(): Triple<MenuEntity, List<CategoryEntity>, List<MenuItemEntity>> {

    val menuEntity = MenuEntity(id = 1)

    val categoryEntities = categories.map { category ->
        CategoryEntity(
            id = category.id,
            menuId = 1,
            name = category.name
        )
    }

    val itemEntities = categories.flatMap { category ->
        category.items.map { item ->
            MenuItemEntity(
                id = item.id,
                categoryId = category.id,
                name = item.name,
                halfPrice = item.halfPrice,
                fullPrice = item.fullPrice,
                description = item.description.orEmpty(),
                imageUrl = item.imageUrl
            )
        }
    }

    return Triple(menuEntity, categoryEntities, itemEntities)
}


fun MenuWithCategories.toDomain(): Menu {
    return Menu(
        categories = categories.map { it.toDomain() }
    )
}
fun CategoryWithItems.toDomain(): Category {
    return Category(
        id = category.id,
        name = category.name,
        menuId = category.menuId,
        items = items.map { it.toDomain() }
    )
}
fun MenuItemEntity.toDomain(): MenuItem {
    return MenuItem(
        id = id,
        name = name,
        halfPrice = halfPrice,
        fullPrice = fullPrice,
        description = description,
        imageUrl = imageUrl
    )
}




fun Category.toEntity(menuId: Int) = CategoryEntity(id, menuId, name)

fun MenuItem.toEntity(categoryId: Int) =
    MenuItemEntity(
        id,
        categoryId,
        name,
        halfPrice,
        fullPrice,
        description.orEmpty(),
        imageUrl
    )

// data/mapper/MenuDocumentMapper.kt
fun MenuDocument.menuDocumentToEntities(): Triple<
        MenuEntity,
        List<CategoryEntity>,
        List<MenuItemEntity>
        > {
    val menuEntity = MenuEntity(id = 1)

    val categoryEntities = categories.map { category ->
        CategoryEntity(
            id = category.id,
            menuId = 1,
            name = category.name
        )
    }

    val itemEntities = categories.flatMap { category ->
        category.items.map { item ->
            MenuItemEntity(
                id = item.id,
                categoryId = category.id,
                name = item.name,
                halfPrice = item.halfPrice ,
                fullPrice = item.fullPrice ,
                description = item.description.orEmpty(),
                imageUrl = item.imageUrl
            )
        }
    }

    return Triple(menuEntity, categoryEntities, itemEntities)
}









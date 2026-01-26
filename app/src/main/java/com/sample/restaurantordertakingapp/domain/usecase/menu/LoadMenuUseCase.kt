package com.sample.restaurantordertakingapp.domain.usecase.menu

import com.sample.restaurantordertakingapp.domain.model.Menu
import com.sample.restaurantordertakingapp.domain.repo.MenuRepository
import com.sample.restaurantordertakingapp.network.Resource
import kotlinx.coroutines.flow.Flow

class LoadMenuUseCase(
    private val menuRepository: MenuRepository
) {
    operator fun invoke(): Flow<Resource<Menu>> {
        return menuRepository.fetchMenu()
    }
}

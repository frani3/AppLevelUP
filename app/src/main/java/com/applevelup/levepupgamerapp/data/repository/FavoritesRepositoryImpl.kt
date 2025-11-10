package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.prefs.FavoritePreferencesDataSource
import com.applevelup.levepupgamerapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FavoritesRepositoryImpl(
    private val dataSource: FavoritePreferencesDataSource = LevelUpApplication.favoritePreferences
) : FavoritesRepository {

    override fun observeFavoriteIds(): Flow<Set<Int>> = dataSource.favoriteIdsFlow

    override suspend fun isFavorite(productId: Int): Boolean {
        return dataSource.favoriteIdsFlow.first().contains(productId)
    }

    override suspend fun addFavorite(productId: Int) {
        dataSource.setFavorite(productId, true)
    }

    override suspend fun removeFavorite(productId: Int) {
        dataSource.setFavorite(productId, false)
    }

    override suspend fun toggleFavorite(productId: Int) {
        dataSource.toggleFavorite(productId)
    }
}

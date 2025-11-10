package com.applevelup.levepupgamerapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavoriteIds(): Flow<Set<Int>>
    suspend fun isFavorite(productId: Int): Boolean
    suspend fun addFavorite(productId: Int)
    suspend fun removeFavorite(productId: Int)
    suspend fun toggleFavorite(productId: Int)
}

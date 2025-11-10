package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.prefs.FavoritePreferencesDataSource
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class FavoritesRepositoryImpl(
    private val dataSource: FavoritePreferencesDataSource = LevelUpApplication.favoritePreferences,
    private val sessionPrefs: SessionPreferencesDataSource = LevelUpApplication.sessionPreferences
) : FavoritesRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeFavoriteIds(): Flow<Set<Int>> {
        return sessionPrefs.sessionFlow.flatMapLatest { session ->
            dataSource.favoriteIdsFlow(userKey(session))
        }
    }

    override suspend fun isFavorite(productId: Int): Boolean {
        val key = currentUserKey()
        return dataSource.currentFavorites(key).contains(productId)
    }

    override suspend fun addFavorite(productId: Int) {
        val key = currentUserKey()
        dataSource.setFavorite(key, productId, true)
    }

    override suspend fun removeFavorite(productId: Int) {
        val key = currentUserKey()
        dataSource.setFavorite(key, productId, false)
    }

    override suspend fun toggleFavorite(productId: Int) {
        val key = currentUserKey()
        dataSource.toggleFavorite(key, productId)
    }

    private suspend fun currentUserKey(): String {
        val session = sessionPrefs.sessionFlow.first()
        return userKey(session)
    }

    private fun userKey(session: SessionState): String {
        session.userId?.let { return "id:$it" }
        session.email?.takeIf { it.isNotBlank() }?.let { return "email:$it" }
        return GUEST_KEY
    }

    companion object {
        private const val GUEST_KEY = "guest"
    }
}

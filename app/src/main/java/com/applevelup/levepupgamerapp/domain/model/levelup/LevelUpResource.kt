package com.applevelup.levepupgamerapp.domain.model.levelup

sealed interface LevelUpResource<out T> {
    object Loading : LevelUpResource<Nothing>
    data class Success<out T>(val data: T) : LevelUpResource<T>
    data class Error<out T>(val throwable: Throwable, val data: T? = null) : LevelUpResource<T>
}

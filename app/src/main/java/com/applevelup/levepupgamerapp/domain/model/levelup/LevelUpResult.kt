package com.applevelup.levepupgamerapp.domain.model.levelup

sealed interface LevelUpResult<out T> {
    data class Success<out T>(val data: T) : LevelUpResult<T>
    data class Failure(val throwable: Throwable) : LevelUpResult<Nothing>
}

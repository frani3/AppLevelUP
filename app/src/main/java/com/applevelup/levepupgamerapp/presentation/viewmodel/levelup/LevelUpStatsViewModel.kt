package com.applevelup.levepupgamerapp.presentation.viewmodel.levelup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpStats
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchLevelUpStatsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LevelUpStatsViewModel(private val useCase: FetchLevelUpStatsUseCase) : ViewModel() {

    private val _statsState = MutableStateFlow<LevelUpResource<LevelUpStats>>(LevelUpResource.Loading)
    val statsState: StateFlow<LevelUpResource<LevelUpStats>> = _statsState

    fun fetchStats(run: String) {
        viewModelScope.launch {
            _statsState.value = LevelUpResource.Loading
            when (val result = useCase(run)) {
                is LevelUpResult.Success -> _statsState.value = LevelUpResource.Success(result.data)
                is LevelUpResult.Failure -> _statsState.value = LevelUpResource.Error(result.throwable)
            }
        }
    }
}

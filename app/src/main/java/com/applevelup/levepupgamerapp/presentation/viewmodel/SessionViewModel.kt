package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(
    private val repository: SessionRepository = SessionRepositoryImpl()
) : ViewModel() {

    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState

    init {
        viewModelScope.launch {
            repository.observeSession().collect { state ->
                _sessionState.value = state
            }
        }
    }
}

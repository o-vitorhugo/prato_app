package com.example.appprato.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appprato.domain.repository.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    repository: NotificationsRepository
) : ViewModel() {

    // Expõe o estado da UI, que é um reflexo direto do fluxo de dados do repositório.
    val uiState: StateFlow<NotificationsUiState> = repository.getNotifications()
        .map { notifications -> NotificationsUiState(notifications = notifications) } // Mapeia a lista para o estado da UI
        .catch { e -> emit(NotificationsUiState(error = e.message)) } // Em caso de erro, atualiza o estado
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotificationsUiState(isLoading = true) // Estado inicial de carregamento
        )
}

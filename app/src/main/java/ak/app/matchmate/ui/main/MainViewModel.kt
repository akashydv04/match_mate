package ak.app.matchmate.ui.main

import ak.app.matchmate.data.local.model.UserEntity
import ak.app.matchmate.data.mappers.UserMappers.toMatchCardUiModel
import ak.app.matchmate.data.repository.UserRepository
import ak.app.matchmate.ui.model.MatchCardUiModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _matchCards = MutableStateFlow<List<MatchCardUiModel>>(emptyList())
    val matchCards: StateFlow<List<MatchCardUiModel>> = _matchCards.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _showImages = MutableStateFlow(true) // Default to true (show images)
    val showImages: StateFlow<Boolean> = _showImages.asStateFlow()


    init {

        observeLocalMatchCards()
        refreshMatchCards()
    }

    private fun observeLocalMatchCards() {
        viewModelScope.launch {
            userRepository.getMatchCards().collectLatest { userEntities ->

                _matchCards.value = userEntities.map { it.toMatchCardUiModel() }
            }
        }
    }

    fun refreshMatchCards() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Clear previous errors
            try {
                userRepository.refreshUsers()
            } catch (e: IOException) {
                _errorMessage.value = "Network error: ${e.message}. Showing cached data."
            } catch (e: Exception) {
                _errorMessage.value = "An unexpected error occurred: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun acceptUser(uuid: String) {
        viewModelScope.launch {
            _errorMessage.value = null // Clear any previous error before action
            try {
                userRepository.updateUserStatus(uuid, "ACCEPTED")
            } catch (e: Exception) {
                _errorMessage.value = "Failed to accept user: ${e.message}"
            }
        }
    }

    fun declineUser(uuid: String) {
        viewModelScope.launch {
            _errorMessage.value = null // Clear any previous error before action
            try {
                userRepository.updateUserStatus(uuid, "DECLINED")
            } catch (e: Exception) {
                _errorMessage.value = "Failed to decline user: ${e.message}"
            }
        }
    }

    // Function to toggle image visibility (for the extra challenge)
    fun toggleImageVisibility() {
        _showImages.value = !_showImages.value
    }
}
package aunn.gg.rest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import aunn.gg.rest.repository.UserListRepository
import kotlinx.coroutines.launch
import java.io.IOException

class UserListViewModel(application: Application, userId: String) : AbstractViewModel(application) {

    private val userListRepository = UserListRepository(application)

    init {
        refreshDataFromRepository(userId)
    }

    private val userList = userListRepository.userList

    private fun refreshDataFromRepository(userId: String) {
        viewModelScope.launch {
            try {
                userListRepository.refreshUserList(userId)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (userList.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    class Factory(val app: Application, val userId: String = "") : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserListViewModel(app, userId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
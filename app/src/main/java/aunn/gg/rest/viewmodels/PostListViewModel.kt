package aunn.gg.rest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import aunn.gg.rest.repository.PostListRepository
import kotlinx.coroutines.launch
import java.io.IOException

class PostListViewModel(application: Application) : AbstractViewModel(application) {

    private val postListRepository =
        PostListRepository(application) // check import for getDatabase method

    init {
        refreshDataFromRepository()
    }

    val postList = postListRepository.postList

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                postListRepository.refreshPostList()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (postList.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PostListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
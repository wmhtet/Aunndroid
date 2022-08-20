package aunn.gg.rest.viewmodels

import androidx.lifecycle.viewModelScope
import aunn.gg.rest.repository.PostListRepository
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class PostListViewModel @Inject constructor(
    private val postListRepository: PostListRepository
) : AbstractViewModel() {

    //private val postListRepository = PostListRepository(application)
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

    /*
    class Factory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PostListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PostListViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
    */
}
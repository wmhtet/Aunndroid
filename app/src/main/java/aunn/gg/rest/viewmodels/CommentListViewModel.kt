package aunn.gg.rest.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import aunn.gg.rest.repository.CommentListRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class CommentListViewModel(context: Context, postId: String) :
    AbstractViewModel() {
    private val commentListRepository = CommentListRepository(context, postId)

    init {
        refreshDataFromRepository(postId)
    }

    val commentList = commentListRepository.commentList

    private fun refreshDataFromRepository(postId: String) {
        viewModelScope.launch {
            try {
                commentListRepository.refreshCommentList(postId)
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: Exception) {
                // Show a Toast error message and hide the progress bar.
                Timber.e(networkError)
                if (commentList.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    class Factory(val context: Context, val postId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommentListViewModel(context, postId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
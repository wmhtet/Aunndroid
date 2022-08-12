package aunn.gg.rest.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import aunn.gg.rest.repository.CommentListRepository
import kotlinx.coroutines.launch
import java.io.IOException

class CommentListViewModel(application: Application, postId: String) :
    AbstractViewModel(application) {

    private val commentListRepository = CommentListRepository(application, postId)

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

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (commentList.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    class Factory(val app: Application, val postId: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CommentListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CommentListViewModel(app, postId) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
}
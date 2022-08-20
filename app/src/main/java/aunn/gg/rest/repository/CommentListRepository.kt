package aunn.gg.rest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import aunn.gg.rest.database.asDatabaseModelCommentList
import aunn.gg.rest.database.asDomainModelCommentList
import aunn.gg.rest.database.getCommentListDatabase
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.network.NetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CommentListRepository(context: Context, postId: String) : Repository(context) {
    private val database = getCommentListDatabase(context)
    private val network = NetworkCall()
    val commentList: LiveData<List<Comment>> =
        Transformations.map(database.commentDao.getComments(postId)) {
            it.asDomainModelCommentList()
        }

    suspend fun refreshCommentList(postId: String) {
        withContext(Dispatchers.IO + exceptionHandler) {
            val playlist = network.getCommentList(postId)
            // database.clearAllTables()
            playlist?.let { database.commentDao.insertAll(asDatabaseModelCommentList(playlist)) }
        }
    }
}
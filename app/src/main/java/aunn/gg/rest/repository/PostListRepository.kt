package aunn.gg.rest.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import aunn.gg.rest.database.asDatabaseModelPostList
import aunn.gg.rest.database.asDomainModelPostList
import aunn.gg.rest.database.getPostListDatabase
import aunn.gg.rest.domain.Post
import aunn.gg.rest.network.NetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostListRepository(application: Application) : Repository(application) {
    private val database = getPostListDatabase(application)
    private val network = NetworkCall()
    val postList: LiveData<List<Post>> = Transformations.map(database.postDao.getPosts()) {
        it.asDomainModelPostList()
    }

    suspend fun refreshPostList() {
        withContext(Dispatchers.IO + exceptionHandler) {
            val playlist = network.getPostList()
            playlist?.let {
                database.clearAllTables()
                database.postDao.insertAll(asDatabaseModelPostList(playlist))
            }
        }
    }
}
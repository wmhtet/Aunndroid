package aunn.gg.rest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import aunn.gg.rest.database.PostListDatabase
import aunn.gg.rest.database.asDatabaseModelPostList
import aunn.gg.rest.database.asDomainModelPostList
import aunn.gg.rest.di.AppModule
import aunn.gg.rest.domain.Post
import aunn.gg.rest.network.RestClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostListRepository @Inject constructor(
    @AppModule.PostListLocal private val database: PostListDatabase,
    @AppModule.PostListRemote private val network: RestClient,
    context: Context
) : Repository(context.applicationContext) {
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
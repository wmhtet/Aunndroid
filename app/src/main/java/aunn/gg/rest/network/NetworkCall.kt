package aunn.gg.rest.network

import android.util.Log
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.domain.Post
import aunn.gg.rest.domain.User

class NetworkCall {
    private val TAG: String? = NetworkCall::class.simpleName
    private val restClient: RestClient by lazy {
        if (change) {
            change = !change
            Retrofit()
        } else {
            change = !change
            Okhttp()
        }
    }

    suspend fun fetch(url: String, parameters: Map<String, String>?): String? {
        return restClient.fetch(url, parameters)
    }

    suspend fun getPostList(): List<Post> {
        return restClient.getPostList()
    }

    suspend fun getUser(userId: String): User? {
        return restClient.getUser(userId)
    }

    suspend fun getUserList(): List<User> {
        return restClient.getUserList()
    }

    suspend fun getCommentList(postId: String): List<Comment> {
        Log.d(TAG, "${this::getCommentList.name} : ")
        return restClient.getCommentList(postId)
    }

    companion object {
        private var change = false
    }
}
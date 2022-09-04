package aunn.gg.rest.network

import aunn.gg.rest.domain.Comment
import aunn.gg.rest.domain.Post
import aunn.gg.rest.domain.User
import timber.log.Timber

class NetworkCall(hostName: String = productionHostName) : RestClient {
    private val restClient: RestClient by lazy {
        if (change) {
            change = !change
            Retrofit(hostName)
        } else {
            change = !change
            Okhttp(hostName)
        }
    }

    override suspend fun fetch(url: String, parameters: Map<String, String>?): String? {
        return restClient.fetch(url, parameters)
    }

    override suspend fun getPostList(): List<Post>? {
        return restClient.getPostList()
    }

    override suspend fun getUser(userId: String): User? {
        return restClient.getUser(userId)
    }

    override suspend fun getUserList(): List<User>? {
        return restClient.getUserList()
    }

    override suspend fun getCommentList(postId: String): List<Comment>? {
        Timber.d("${this::getCommentList.name} : $postId")
        return restClient.getCommentList(postId)
    }

    companion object {
        private var change = false
    }
}
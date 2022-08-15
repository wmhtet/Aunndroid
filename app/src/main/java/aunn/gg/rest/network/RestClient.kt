package aunn.gg.rest.network

import aunn.gg.rest.domain.Comment
import aunn.gg.rest.domain.Post
import aunn.gg.rest.domain.User

interface RestClient {
    suspend fun fetch(url: String, parameters: Map<String, String>?): String?
    suspend fun getPostList(): List<Post>?
    suspend fun getUser(userId: String): User?
    suspend fun getUserList(): List<User>?
    suspend fun getCommentList(postId: String): List<Comment>?
}
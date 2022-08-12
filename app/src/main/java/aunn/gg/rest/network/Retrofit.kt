package aunn.gg.rest.network

import android.util.Log
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.domain.Post
import aunn.gg.rest.domain.User
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {
    @GET(postUrl)
    suspend fun getPostList(): List<Post>

    @GET("$userUrl/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @GET(userUrl)
    suspend fun getUserList(): List<User>

    @GET(commentUrl)
    suspend fun getComment(@Query(value = "postId", encoded = true) postId: String): List<Comment>
}

class Retrofit : RestClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(hostname)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val service = retrofit.create(Service::class.java)
    private val TAG: String? = Retrofit::class.simpleName

    override suspend fun fetch(url: String, parameters: Map<String, String>?): String? {
        TODO("Not yet implemented")
    }

    override suspend fun getPostList(): List<Post> {
        Log.d(TAG, this::getPostList.name)
        return service.getPostList()
    }

    override suspend fun getUser(userId: String): User {
        return service.getUser(userId)
    }

    override suspend fun getUserList(): List<User> {
        return service.getUserList()
    }

    override suspend fun getCommentList(postId: String): List<Comment> {
        Log.d(TAG, "${this::getCommentList.name} : $postId")
        return service.getComment(postId)
    }
}
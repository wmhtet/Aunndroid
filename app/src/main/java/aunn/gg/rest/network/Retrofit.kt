package aunn.gg.rest.network

import android.util.Log
import aunn.gg.rest.domain.Comment
import aunn.gg.rest.domain.Post
import aunn.gg.rest.domain.User
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.net.HttpURLConnection

interface Service {

    @GET
    suspend fun fetch(@Url url:String): Response<String>

    @GET(postUrl)
    suspend fun getPostList(): Response<List<Post>>

    @GET("$userUrl/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<User>

    @GET(userUrl)
    suspend fun getUserList(): Response<List<User>>

    @GET(commentUrl)
    suspend fun getComment(@Query(value = "postId", encoded = true) postId: String):
            Response<List<Comment>>
}

class Retrofit(hostName: String) : RestClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(hostName)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    private val service = retrofit.create(Service::class.java)
    private val TAG: String? = Retrofit::class.simpleName

    override suspend fun fetch(url: String, parameters: Map<String, String>?): String? {
        val response = service.fetch(url)
        throwNetworkError(response.code(), "${this::fetch.name} $url")
        return response.body()
    }

    override suspend fun getPostList(): List<Post>? {
        // Log.d(TAG, this::getPostList.name)
        val response =  service.getPostList()
        throwNetworkError(response.code(), this::getPostList.name)
        return response.body()
    }

    override suspend fun getUser(userId: String): User? {
        val response =  service.getUser(userId)
        throwNetworkError(response.code(), this::getUser.name)
        return response.body()
    }

    override suspend fun getUserList(): List<User>? {
        val response =  service.getUserList()
        throwNetworkError(response.code(), this::getUserList.name)
        return response.body()
    }

    override suspend fun getCommentList(postId: String): List<Comment>? {
        Log.d(TAG, "${this::getCommentList.name} : $postId")
        val response =  service.getComment(postId)
        throwNetworkError(response.code(), this::getCommentList.name)
        return response.body()
    }

    private fun throwNetworkError(response_code:Int, method:String){
        if (response_code != HttpURLConnection.HTTP_OK){
            Log.e(TAG, "$method : $response_code")
            val networkError = "Network Error.".toResponseBody("plain/text".toMediaTypeOrNull())
            throw HttpException(Response.error<ResponseBody>(response_code, networkError))
        }
    }
}
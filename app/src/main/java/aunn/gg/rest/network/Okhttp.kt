package aunn.gg.rest.network

import aunn.gg.rest.domain.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.net.HttpURLConnection

class Okhttp(private val hostName: String) : RestClient {
    private var client: OkHttpClient = OkHttpClient()

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun fetch(url: String, parameters: Map<String, String>?): String {
        Timber.d("${this::fetch.name} : $url")
        val result: String?
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        result = response.body.string()
        val responseCode = response.code
        if (responseCode != HttpURLConnection.HTTP_OK) {
            Timber.e("${this::fetch.name} : $responseCode $url ")
            val networkError = "Network Error.".toResponseBody("plain/text".toMediaTypeOrNull())
            throw HttpException(Response.error<ResponseBody>(responseCode, networkError))
        }
        return result
    }

    override suspend fun getPostList(): List<Post> {
        val str = fetch(hostName + postUrl, null)
        str.let { return getPostsFromJson(str) }
    }

    override suspend fun getUser(userId: String): User {
        Timber.d("${this::getUser.name} : $userId")
        val str = fetch("$hostName$userUrl/$userId", null)
        str.let { return getUserFromJson(str) }
    }

    override suspend fun getUserList(): List<User> {
        Timber.d("${this::getUserList.name} ")
        val str = fetch(hostName + userUrl, null)
        str.let { return getUserListFromJson(str) }
    }

    override suspend fun getCommentList(postId: String): List<Comment> {
        Timber.d("${this::getCommentList.name} : $postId")
        val str = fetch(hostName + commentUrl + "postId=" + postId, null)
        str.let { return getCommentsFromJson(str) }
    }
}
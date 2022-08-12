package aunn.gg.rest.network

import android.util.Log
import aunn.gg.rest.domain.*
import okhttp3.OkHttpClient
import okhttp3.Request

class Okhttp : RestClient {
    private var client: OkHttpClient = OkHttpClient()
    private val TAG: String? = Okhttp::class.simpleName

    override suspend fun fetch(url: String, parameters: Map<String, String>?): String? {
        Log.d(TAG, "${this::fetch.name} : $url")
        var result: String? = null
        try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            result = response.body.string()
        } catch (err: Error) {
            print("Error when executing get request: $err")
        }
        return result
    }

    override suspend fun getPostList(): List<Post> {
        val str = fetch(hostname + postUrl, null)
        str?.let { return getPostsFromJson(str) }
        return ArrayList()
    }

    override suspend fun getUser(userId: String): User? {
        Log.d(TAG, "${this::getUser.name} : $userId")
        val str = fetch("$hostname$userUrl/$userId", null)
        str?.let { return getUserFromJson(str) }
        return null
    }

    override suspend fun getUserList(): List<User> {
        Log.d(TAG, "${this::getUserList.name} ")
        val str = fetch(hostname + userUrl, null)
        str?.let { return getUserListFromJson(str) }
        return ArrayList()
    }

    override suspend fun getCommentList(postId: String): List<Comment> {
        Log.d(TAG, "${this::getCommentList.name} : $postId")
        val str = fetch(hostname + commentUrl + "postId=" + postId, null)
        str?.let { return getCommentsFromJson(str) }
        return ArrayList()
    }
}
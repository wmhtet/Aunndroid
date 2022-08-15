package aunn.gg.rest

import aunn.gg.rest.network.Okhttp
import aunn.gg.rest.network.Retrofit
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.EOFException
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import java.net.HttpURLConnection


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RestClientUnitTest {
    private lateinit var mockWebServer : MockWebServer

    @Before
    fun initTest(){
        mockWebServer = MockWebServer()
    }

    @After
    fun shutdown(){
        mockWebServer.shutdown()
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `read simple file`(){
        val reader = MockResponseFileReader("test.txt")
        assertEquals(reader.content, "success")
    }

    @Test
    fun `retrofit read file from web`(){
        val reader = MockResponseFileReader("test.txt")
        val mockResponse = MockResponse().setBody(reader.content)
        mockWebServer.enqueue(mockResponse)
        val baseUrl = mockWebServer.url("")
        val retrofit = Retrofit(baseUrl.toUrl().toString())
        runBlocking{
            assertEquals(reader.content, retrofit.fetch(baseUrl.toUrl().toString(), null))
        }
    }

    @Test
    fun `okhttp read file from web`(){
        val reader = MockResponseFileReader("test.txt")
        val mockResponse = MockResponse().setBody(reader.content)
        mockWebServer.enqueue(mockResponse)
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        runBlocking{
            assertEquals(reader.content, okhttp.fetch(baseUrl.toUrl().toString(), null))
        }
    }

    @Test
    fun `read posts from web`(){
        val reader = MockResponseFileReader("posts.json")
        val mockResponse = MockResponse().setBody(reader.content)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.enqueue(mockResponse)
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        val retrofit = Retrofit(baseUrl.toUrl().toString())
        runBlocking{
            val retroPosts = retrofit.getPostList()
            val okPosts = okhttp.getPostList()
            assertEquals(okPosts, retroPosts)
        }
    }

    @Test
    fun `read Comments from web`(){
        val reader = MockResponseFileReader("comments.json")
        val mockResponse = MockResponse().setBody(reader.content)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.enqueue(mockResponse)
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        val retrofit = Retrofit(baseUrl.toUrl().toString())
        runBlocking{
            val retroComments = retrofit.getCommentList("")
            val okComments = okhttp.getCommentList("")
            assertEquals(okComments, retroComments)
        }
    }

    @Test
    fun `read Users from web`(){
        val reader = MockResponseFileReader("users.json")
        val mockResponse = MockResponse().setBody(reader.content)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.enqueue(mockResponse)
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        val retrofit = Retrofit(baseUrl.toUrl().toString())
        runBlocking{
            val retroUsers = retrofit.getUserList()
            val okUsers = okhttp.getUserList()
            assertEquals(okUsers, retroUsers)
        }
    }

    @Test
    fun `test comments for postId`() {
        val dispatcher: Dispatcher = object : Dispatcher() {
            val content1 = MockResponseFileReader("comments1.json").content
            val content2 = MockResponseFileReader("comments2.json").content
            val content3 = MockResponseFileReader("comments3.json").content
            override fun dispatch(request: RecordedRequest): MockResponse {
                when (request.path) {
                    // for okhttp
                    "/comments?postId=1" -> return MockResponse().setBody(content1)
                    "/comments?postId=2" -> return MockResponse().setBody(content2)
                    "/comments?postId=3" -> return MockResponse().setBody(content3)
                    // for retrofit
                    "/comments?&postId=1" -> return MockResponse().setBody(content1)
                    "/comments?&postId=2" -> return MockResponse().setBody(content2)
                    "/comments?&postId=3" -> return MockResponse().setBody(content3)
                }
                return MockResponse()
            }
        }
        mockWebServer.dispatcher = dispatcher
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        val retrofit = Retrofit(baseUrl.toUrl().toString())
        runBlocking{
            val okComments = okhttp.getCommentList("1")
            assertEquals(okComments[0].postId, "1")
        }
        runBlocking{
            val retroComments = retrofit.getCommentList("2")
            retroComments?.let { assertEquals(retroComments[0].postId, "2") }

        }
        runBlocking{
            val retroComments = retrofit.getCommentList("3")
            retroComments?.let { assertEquals(retroComments[0].postId, "3") }
        }

        assertThrows(org.json.JSONException::class.java) {
            runBlocking{
                okhttp.getCommentList("4")
            }}
    }


    @Test
    fun `test comments for http code`() {
        val dispatcher: Dispatcher = object : Dispatcher() {
            val content3 = MockResponseFileReader("comments3.json").content
            override fun dispatch(request: RecordedRequest): MockResponse {
                when (request.path) {
                    // for okhttp
                    "/comments?postId=1" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                    "/comments?postId=2" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                    "/comments?postId=3" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK).setBody(content3)
                    // for retrofit
                    "/comments?&postId=1" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                    "/comments?&postId=2" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_INTERNAL_ERROR)
                    "/comments?&postId=3" -> return MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK).setBody(content3)
                }
                return MockResponse().setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
            }
        }
        mockWebServer.dispatcher = dispatcher
        val baseUrl = mockWebServer.url("")
        val okhttp = Okhttp(baseUrl.toUrl().toString())
        val retrofit = Retrofit(baseUrl.toUrl().toString())

        // Http Ok but no data
        assertThrows(org.json.JSONException::class.java) {
            runBlocking {
                okhttp.getCommentList("1")
            }
        }

        // HTTP_INTERNAL_ERROR
        assertThrows(HttpException::class.java) {
            runBlocking {
                okhttp.getCommentList("2")
            }
        }

        // HTTP_NOT_FOUND
        assertThrows(HttpException::class.java) {
            runBlocking {
                okhttp.getCommentList("4")
            }
        }

        // Retrofit does not throw Json error but End of File
        assertThrows(EOFException::class.java) {
            runBlocking{
                retrofit.getCommentList("1")
            }}

        // HTTP_INTERNAL_ERROR
        assertThrows(HttpException::class.java) {
            runBlocking{
                retrofit.getCommentList("2")
            }}

        // HTTP_NOT_FOUND
        assertThrows(HttpException::class.java) {
            runBlocking{
                retrofit.getCommentList("4")
            }}
    }

}
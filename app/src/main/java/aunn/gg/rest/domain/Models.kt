package aunn.gg.rest.domain

import android.util.Log
import com.squareup.moshi.JsonClass
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener

private const val TAG = "aunn.gg.rest.domain"

@JsonClass(generateAdapter = true)
data class Post(
    val id: String,
    val userId: String,
    val title: String,
    val body: String
)

@JsonClass(generateAdapter = true)
data class Comment(
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String
)

fun getPostsFromJson(jsonString: String): List<Post> {
    Log.d(TAG, "getPostsFromJson $jsonString")
    val posts: ArrayList<Post> = ArrayList()
    // println(jsonString)
    val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
    for (i in 0 until jsonArray.length()) {
        val id = jsonArray.getJSONObject(i).getInt("id").toString()
        val userId = jsonArray.getJSONObject(i).getInt("userId").toString()
        val title = jsonArray.getJSONObject(i).getString("title")
        val body = jsonArray.getJSONObject(i).getString("body")
        posts.add(Post(id, userId, title, body))
    }
    return posts
}

fun getCommentsFromJson(jsonString: String): List<Comment> {
    Log.d(TAG, "getCommentsFromJson")
    val comments: ArrayList<Comment> = ArrayList()
    val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
    for (i in 0 until jsonArray.length()) {
        val id = jsonArray.getJSONObject(i).getInt("id").toString()
        val postId = jsonArray.getJSONObject(i).getInt("postId").toString()
        val name = jsonArray.getJSONObject(i).getString("name")
        val email = jsonArray.getJSONObject(i).getString("email")
        val body = jsonArray.getJSONObject(i).getString("body")
        comments.add(Comment(id, postId, name, email, body))
    }
    return comments
}

@JsonClass(generateAdapter = true)
data class Geo(val lat: String, val lng: String)

@JsonClass(generateAdapter = true)
data class Address(
    val street: String, val suite: String, val city: String, val zipcode: String, val geo: Geo
)

fun getAddressFromJson(jsonobj: JSONObject): Address {
    val street = jsonobj.getString("street")
    val suite = jsonobj.getString("suite")
    val city = jsonobj.getString("city")
    val zipcode = jsonobj.getString("zipcode")
    val geoJsonObj = jsonobj.getJSONObject("geo")
    val geo = getGeoFromJson(geoJsonObj)
    return Address(street, suite, city, zipcode, geo)
}

fun getGeoFromJson(jsonObj: JSONObject): Geo {
    val lat = jsonObj.getString("lat")
    val lng = jsonObj.getString("lng")
    return Geo(lat, lng)
}

@JsonClass(generateAdapter = true)
data class Company(val name: String, val catchPhrase: String, val bs: String)

fun getCompanyFromJson(jsonObject: JSONObject): Company {
    val name = jsonObject.getString("name")
    val catchPhrase = jsonObject.getString("catchPhrase")
    val bs = jsonObject.getString("bs")
    return Company(name, catchPhrase, bs)
}


@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
)

fun getUserFromJson(jsonString: String): User {
    val jsonObject = JSONTokener(jsonString).nextValue() as JSONObject
    return getUserFromJson(jsonObject)
}

fun getUserFromJson(jsonObject: JSONObject): User {
    val id = jsonObject.getInt("id").toString()
    val name = jsonObject.getString("name")
    val username = jsonObject.getString("username")
    val email = jsonObject.getString("email")
    val addressJsonobj = jsonObject.getJSONObject("address")
    val address = getAddressFromJson(addressJsonobj)
    val phone = jsonObject.getString("phone")
    val website = jsonObject.getString("website")
    val companyJsonObject = jsonObject.getJSONObject("company")
    val company = getCompanyFromJson(companyJsonObject)
    return User(id, name, username, email, address, phone, website, company)
}


fun getUserListFromJson(jsonString: String): List<User> {
    Log.d(TAG, "getUserListFromJson")
    val users: ArrayList<User> = ArrayList()
    val jsonArray = JSONTokener(jsonString).nextValue() as JSONArray
    for (i in 0 until jsonArray.length()) {
        users.add(getUserFromJson(jsonArray.getJSONObject(i)))
    }
    return users
}
package aunn.gg.rest.database

import android.util.Log
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import aunn.gg.rest.domain.*

const val TAG ="aunn.gg.rest.database"

@Entity
data class DatabasePost constructor(
    @PrimaryKey
    val id: String,
    val userId: String,
    val title: String,
    val body: String
)

fun List<DatabasePost>.asDomainModelPostList(): List<Post> {
    Log.d(TAG, "asDomainModelPostList")
    return map { Post(id = it.id, userId = it.userId, title = it.title, body = it.body) }
}

fun asDatabaseModelPostList(postList: List<Post>): List<DatabasePost> {
    return postList.map {
        DatabasePost(id = it.id, userId = it.userId, title = it.title, body = it.body)
    }
}

@Entity
data class DatabaseComment constructor(
    @PrimaryKey
    val id: String,
    val postId: String,
    val name: String,
    val email: String,
    val body: String
)

fun List<DatabaseComment>.asDomainModelCommentList(): List<Comment> {
    return map {
        // Log.d(TAG, "asDomainModelCommentList : $it")
        Comment(id = it.id, postId = it.postId, name = it.name, email = it.email, body = it.body)
    }
}

fun asDatabaseModelCommentList(commentList: List<Comment>): List<DatabaseComment> {
    return commentList.map {
        DatabaseComment(
            id = it.id, postId = it.postId, name = it.name, email = it.email, body = it.body
        )
    }
}

data class DatabaseGeo(val lat: String, val lng: String)

fun DatabaseGeo.asDomainModelGeo(): Geo {
    return Geo(lat, lng)
}

fun Geo.asDatabaseModelGeo(): DatabaseGeo {
    return DatabaseGeo(lat, lng)
}

data class DatabaseAddress(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    @Embedded
    val geo: DatabaseGeo
)

fun DatabaseAddress.asDomainModelAddress(): Address {
    return Address(street, suite, city, zipcode, geo.asDomainModelGeo())
}

fun Address.asDatabaseModelAddress(): DatabaseAddress {
    return DatabaseAddress(street, suite, city, zipcode, geo.asDatabaseModelGeo())
}

data class DatabaseCompany(val name: String, val catchPhrase: String, val bs: String)

fun DatabaseCompany.asDomainModelCompany(): Company {
    return Company(name, catchPhrase, bs)
}

fun Company.asDatabaseModelCompany(): DatabaseCompany {
    return DatabaseCompany(name, catchPhrase, bs)
}

@Entity
data class DatabaseUser constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    @Embedded(prefix = "address_")
    val address: DatabaseAddress,
    val phone: String,
    val website: String,
    @Embedded(prefix = "company_")
    val company: DatabaseCompany
)

fun DatabaseUser.asDomainModelUser(): User {
    return User(
        id, name, username, email, address.asDomainModelAddress(),
        phone, website, company.asDomainModelCompany()
    )
}

fun User.asDatabaseModelUser(): DatabaseUser {
    return DatabaseUser(
        id, name, username, email, address.asDatabaseModelAddress(),
        phone, website, company.asDatabaseModelCompany()
    )
}

fun List<DatabaseUser>.asDomainModelUserList(): List<User> {
    return map { it.asDomainModelUser() }
}

fun asDatabaseModelUserList(commentList: List<User>): List<DatabaseUser> {
    return commentList.map { it.asDatabaseModelUser() }
}
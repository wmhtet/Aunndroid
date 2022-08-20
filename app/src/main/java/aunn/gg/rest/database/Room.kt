package aunn.gg.rest.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {
    @Query("select * from databasePost")
    fun getPosts(): LiveData<List<DatabasePost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Posts: List<DatabasePost>)
}

@Database(entities = [DatabasePost::class], version = 1)
abstract class PostListDatabase : RoomDatabase() {
    abstract val postDao: PostDao
}

private lateinit var postListDatabase: PostListDatabase


fun getPostListDatabase(context: Context): PostListDatabase {
    synchronized(PostListDatabase::class.java) {
        if (!::postListDatabase.isInitialized) {
            postListDatabase = Room.databaseBuilder(
                context.applicationContext,
                PostListDatabase::class.java,
                "postlist"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return postListDatabase
}


@Dao
interface CommentDao {
    @Query("select * from databaseComment where postId == :postId")
    fun getComments(postId: String): LiveData<List<DatabaseComment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Comments: List<DatabaseComment>)
}

@Database(entities = [DatabaseComment::class], version = 1)
abstract class CommentListDatabase : RoomDatabase() {
    abstract val commentDao: CommentDao
}

private lateinit var commentListDatabase: CommentListDatabase

fun getCommentListDatabase(context: Context): CommentListDatabase {
    synchronized(CommentListDatabase::class.java) {
        if (!::commentListDatabase.isInitialized) {
            commentListDatabase = Room.databaseBuilder(
                context.applicationContext,
                CommentListDatabase::class.java,
                "commentlist"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return commentListDatabase
}


@Dao
interface UserDao {
    @Query("select * from databaseUser")
    fun getUsers(): LiveData<List<DatabaseUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(Users: List<DatabaseUser>)
}

@Database(entities = [DatabaseUser::class], version = 1)
abstract class UserListDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}

private lateinit var userListDatabase: UserListDatabase

fun getUserListDatabase(context: Context): UserListDatabase {
    synchronized(UserListDatabase::class.java) {
        if (!::userListDatabase.isInitialized) {
            userListDatabase = Room.databaseBuilder(
                context.applicationContext,
                UserListDatabase::class.java,
                "userlist"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return userListDatabase
}

package aunn.gg.rest.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import aunn.gg.rest.database.asDatabaseModelUserList
import aunn.gg.rest.database.asDomainModelUserList
import aunn.gg.rest.database.getUserListDatabase
import aunn.gg.rest.domain.User
import aunn.gg.rest.network.NetworkCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserListRepository(context: Context) : Repository(context) {
    private val database = getUserListDatabase(context)
    private val network = NetworkCall()
    val userList: LiveData<List<User>> = Transformations.map(database.userDao.getUsers()) {
        it.asDomainModelUserList()
    }

    suspend fun refreshUserList(userId: String) {
        withContext(Dispatchers.IO + exceptionHandler) {
            val playlist = ArrayList<User>()
            if (userId.isNotEmpty()) {
                val user = network.getUser(userId)
                user?.let { playlist.add(user) }
            } else {
                val list = network.getUserList()
                list?.let { playlist.addAll(list) }
            }
            database.clearAllTables()
            database.userDao.insertAll(asDatabaseModelUserList(playlist))
        }
    }
}
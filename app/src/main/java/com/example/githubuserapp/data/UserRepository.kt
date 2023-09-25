package com.example.githubuserapp.data

import androidx.lifecycle.LiveData
import com.example.githubuserapp.data.database.FavoriteUser
import com.example.githubuserapp.data.database.UserDao

class UserRepository(
    private val userDao: UserDao
) {
    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = userDao.getFavoriteUser()

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = userDao.getFavoriteUserByUsername(username)

    suspend fun insert(favoriteUser: FavoriteUser) {
        userDao.insert(favoriteUser)
    }

    suspend fun delete(favoriteUser: FavoriteUser) {
        userDao.delete(favoriteUser)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userDao)
            }.also { instance = it }
    }
}
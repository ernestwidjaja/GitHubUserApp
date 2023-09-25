package com.example.githubuserapp.di

import android.content.Context
import com.example.githubuserapp.data.UserRepository
import com.example.githubuserapp.data.database.UserRoomDatabase

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = UserRoomDatabase.getInstance(context)
        val dao = database.userDao()
        return UserRepository.getInstance(dao)
    }
}
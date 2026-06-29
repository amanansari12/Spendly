package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository (private val userDao: UserDao){

    suspend fun insertUser(user: UserEntity){

        if (user.name.isBlank()) return

        userDao.insertUser(user)
    }

    fun getUser(): Flow<UserEntity?>{
        return userDao.getUser()
    }
}
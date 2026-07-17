package com.amanansari.spendly.data.repository

import com.amanansari.spendly.data.local.dao.UserDao
import com.amanansari.spendly.data.local.entity.UserEntity
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    suspend fun insertUser(user: UserEntity){

        if (user.name.isBlank()) return

        userDao.insertUser(user)
    }
    fun getUser(): Flow<UserEntity?> = userDao.getUser()
}
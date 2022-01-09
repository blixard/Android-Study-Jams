package com.example.connect.data

import androidx.lifecycle.LiveData
import com.example.connect.data.User
import com.example.connect.data.UserDao

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

}
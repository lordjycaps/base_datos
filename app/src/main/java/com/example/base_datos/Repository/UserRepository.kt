package com.example.base_datos.Repository

import androidx.room.Update
import com.example.base_datos.DAO.UserDAO
import com.example.base_datos.Model.User

class UserRepository(private val userDao: UserDAO) {
    suspend fun insert(user:User){
        userDao.insert(user)
    }
    suspend fun getAllUser(): List<User>{
        return  userDao.getAllUsers()
    }

    suspend fun deleteById(userId: Int): Int {
        return userDao.deleteById(userId) //llama al metodo deleteById del DAO
    }

    suspend fun updateUser(user: User): Int {
        return userDao.update(user) // llama al método update del DAO
    }
}


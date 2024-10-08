package com.example.base_datos.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.base_datos.Model.User

@Dao

interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user : User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query ("DELETE FROM users WHERE id = :userId")
    suspend fun deleteById(userId: Int): Int

    @Update
    suspend fun update(user: User): Int
}
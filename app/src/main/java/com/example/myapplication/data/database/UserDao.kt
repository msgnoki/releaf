package com.example.myapplication.data.database

import androidx.room.*
import com.example.myapplication.data.user.User
import com.example.myapplication.data.user.UserPreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    
    @Query("SELECT * FROM users WHERE uid = :uid")
    suspend fun getUser(uid: String): User?
    
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun getUserFlow(uid: String): Flow<User?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Update
    suspend fun updateUser(user: User)
    
    @Delete
    suspend fun deleteUser(user: User)
    
    @Query("DELETE FROM users WHERE uid = :uid")
    suspend fun deleteUserById(uid: String)
    
    // User Preferences
    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    suspend fun getUserPreferences(userId: String): UserPreferences?
    
    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    fun getUserPreferencesFlow(userId: String): Flow<UserPreferences?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPreferences(preferences: UserPreferences)
    
    @Update
    suspend fun updateUserPreferences(preferences: UserPreferences)
    
    @Query("DELETE FROM user_preferences WHERE userId = :userId")
    suspend fun deleteUserPreferences(userId: String)
}
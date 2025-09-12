package com.releaf.app.data.database

import androidx.room.*
import com.releaf.app.data.user.UserGoal
import com.releaf.app.data.user.GoalType
import kotlinx.coroutines.flow.Flow

@Dao
interface UserGoalDao {
    
    @Query("SELECT * FROM user_goals WHERE userId = :userId AND isActive = 1 ORDER BY startDate DESC")
    fun getActiveUserGoals(userId: String): Flow<List<UserGoal>>
    
    @Query("SELECT * FROM user_goals WHERE userId = :userId ORDER BY startDate DESC")
    fun getAllUserGoals(userId: String): Flow<List<UserGoal>>
    
    @Query("SELECT * FROM user_goals WHERE id = :goalId")
    suspend fun getGoal(goalId: String): UserGoal?
    
    @Query("SELECT * FROM user_goals WHERE userId = :userId AND type = :type AND isActive = 1")
    suspend fun getActiveGoalByType(userId: String, type: GoalType): UserGoal?
    
    @Insert
    suspend fun insertGoal(goal: UserGoal)
    
    @Update
    suspend fun updateGoal(goal: UserGoal)
    
    @Delete
    suspend fun deleteGoal(goal: UserGoal)
    
    @Query("DELETE FROM user_goals WHERE userId = :userId")
    suspend fun deleteUserGoals(userId: String)
    
    @Query("SELECT COUNT(*) FROM user_goals WHERE userId = :userId AND isAchieved = 1")
    suspend fun getAchievedGoalsCount(userId: String): Int
    
    @Query("UPDATE user_goals SET isAchieved = 1, achievedAt = :achievedAt WHERE id = :goalId")
    suspend fun markGoalAsAchieved(goalId: String, achievedAt: Long)
}
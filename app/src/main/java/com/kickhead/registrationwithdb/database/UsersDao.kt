package com.kickhead.registrationwithdb.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kickhead.registrationwithdb.model.UserModel

@Dao
interface UsersDao {
    @Query("SELECT * FROM registered_user")
    fun loadAllChatSessionUsers(): LiveData<MutableList<UserModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChatSessionUser(chatSessionUserModel: UserModel)

    @Update()
    fun updateChatSessionUser(chatSessionUserModel: UserModel)

    @Delete
    fun deleteChatSessionUser(chatSessionUserModel: UserModel)
}

package com.kickhead.registrationwithdb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "registered_user",indices = [Index(value = ["id"], unique = true)])
class UserModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int=0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "phone") var phone: String,
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "date_of_birth") var date_of_birth: String,
    @ColumnInfo(name = "gender") var gender: String,
    @ColumnInfo(name = "age") var age: String,
    @ColumnInfo(name = "math") var math: String,
    @ColumnInfo(name = "english") var english: String,
    @ColumnInfo(name = "science") var science: String,
    @ColumnInfo(name = "selfie_path") var selfie_path: String,
    @ColumnInfo(name = "signature_path") var signature_path: String,
    @ColumnInfo(name = "interest") var interest: String,
    @ColumnInfo(name = "notes") var notes: String
): Serializable

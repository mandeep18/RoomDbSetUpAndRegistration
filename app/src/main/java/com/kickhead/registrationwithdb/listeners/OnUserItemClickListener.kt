package com.kickhead.registrationwithdb.listeners

import com.kickhead.registrationwithdb.model.UserModel

interface OnUserItemClickListener {
    fun onItemClick(userModel: UserModel, position:Int)
}
package com.ntech.theyardhub.datalayer.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.auth.User
import java.sql.Time

data class ChatListModel(
    var name: String = "",
    var message: String = "",
    var messageAt: Timestamp = Timestamp(0, 0),
)

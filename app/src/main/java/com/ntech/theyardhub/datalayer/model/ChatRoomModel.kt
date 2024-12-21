package com.ntech.theyardhub.datalayer.model

import com.google.firebase.Timestamp
import java.sql.Time

data class ChatRoomModel(
    var participants: List<String> = emptyList(),
    var createdAt: Timestamp = Timestamp(0, 0),
    var documentId: String = "",
    var lastMessage: String = "",
    var lastMessageAt: Timestamp = Timestamp(0, 0),
)

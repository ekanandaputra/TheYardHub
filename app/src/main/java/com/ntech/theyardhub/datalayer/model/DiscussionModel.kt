package com.ntech.theyardhub.datalayer.model

import com.google.firebase.Timestamp

data class DiscussionModel(
    val sender: String = "",
    val senderDocumentId: String = "",
    val content: String = "",
    val dateTime: Timestamp = Timestamp(0,0),
    val isMyMessage: Boolean? = null,
)

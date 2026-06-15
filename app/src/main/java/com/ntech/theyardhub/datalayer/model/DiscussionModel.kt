package com.ntech.theyardhub.datalayer.model

import com.google.firebase.Timestamp

data class DiscussionModel(
    val documentId: String = "",
    val parentCommentId: String? = null,
    val replyToName: String? = null,
    val sender: String = "",
    val senderDocumentId: String = "",
    val content: String = "",
    val dateTime: Timestamp = Timestamp(0,0),
    val isMyMessage: Boolean? = null,
)

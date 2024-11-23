package com.ntech.theyardhub.datalayer.model

data class ChatMessageModel(
    val sender: String = "",
    val content: String = "",
    val dateTime: String = "",
    val isMyMessage: Boolean = false,
)

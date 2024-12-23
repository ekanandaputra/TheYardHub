package com.ntech.theyardhub.datalayer.repository

import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.datalayer.model.ChatMessageModel
import com.ntech.theyardhub.datalayer.model.DiscussionModel
import com.ntech.theyardhub.datalayer.model.PostModel


interface PostRepository {

    suspend fun getPosts(): AppResponse<List<PostModel>>

    suspend fun getPost(id: String): AppResponse<PostModel>

    suspend fun getDiscussion(postDocumentId: String): AppResponse<List<DiscussionModel>>

    suspend fun sendDiscussion(
        postDocumentId: String,
        message: String
    ): AppResponse<DiscussionModel>

}
package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class YardRepositoryImp(
    private val productRef: CollectionReference,
    private val dataStorage: DataStorage
) : YardRepository {

}
package com.ntech.theyardhub.datalayer.implementation.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.DataStorage
import com.ntech.theyardhub.datalayer.model.LocationModel
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.datalayer.repository.PostRepository
import com.ntech.theyardhub.datalayer.repository.ProductRepository
import com.ntech.theyardhub.datalayer.repository.YardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class YardRepositoryImp(
    private val yardRef: CollectionReference,
    private val dataStorage: DataStorage
) : YardRepository {
    override suspend fun getFarms(): AppResponse<List<YardModel>> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = yardRef.get().await()
                if (querySnapshot.isEmpty) {
                    return@withContext AppResponse.Empty
                } else {
                    val list = querySnapshot.documents.map { document ->
                        val name = document.get("name") as? String ?: ""
                        val desc = document.get("desc") as? String ?: ""
                        val city = document.get("city") as? String ?: ""

                        YardModel(
                            name = name,
                            description = desc,
                            locationModel = LocationModel(city = city)
                        )
                    }
                    return@withContext AppResponse.Success(list)
                }
            } catch (e: Exception) {
                return@withContext AppResponse.Error(e.toString())
            }
        }
    }
}
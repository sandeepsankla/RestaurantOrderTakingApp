package com.sample.restaurantordertakingapp.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ResourceBodyCallAdapter<T : Any>(
    private val responseType: Type
) : CallAdapter<T, Flow<Resource<T>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        val response = call.execute()  // sync or use suspend call etc.
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(Resource.Success(body))
            } else {
                emit(Resource.Error("Empty body"))
            }
        } else {
            emit(Resource.Error("HTTP ${response.code()} ${response.message()}"))
        }
    }
        .catch { e ->
            emit(Resource.Error(e.localizedMessage ?: "Unknown Error"))
        }
        .flowOn(Dispatchers.IO)
}

class ResourceResponseAdapter<T : Any>(
    private val responseType: Type
) : CallAdapter<T, Flow<Resource<T>>> {

    override fun responseType(): Type = responseType
    override fun adapt(call: Call<T>): Flow<Resource<T>> {
        return flow {
            emit(Resource.Loading)
            val response = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    // Unwrap response and emit the contained T
                    emit(Resource.Success(body))
                } else {
                    emit(Resource.Error("Empty response body"))
                }
            } else {
                val msg = "HTTP ${response.code()} ${response.message()}"
                emit(Resource.Error(msg))
            }
        }
            .catch { e ->
                emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
            }
            .flowOn(Dispatchers.IO)
    }
}

package com.sample.restaurantordertakingapp.network

import kotlinx.coroutines.flow.Flow
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResourceFlowCallAdapterFactory : CallAdapter.Factory() {

    companion object {
        fun create(): ResourceFlowCallAdapterFactory = ResourceFlowCallAdapterFactory()
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        // Check that the return type is Flow<Something>
        val rawType = getRawType(returnType)
        if (rawType != Flow::class.java) {
            return null
        }
        // Check it is parameterized Flow<Something>
        require(returnType is ParameterizedType) { "Return type must be parameterized as Flow<Foo>" }

        // Get inner type T of Flow<T>
        val flowInnerType = getParameterUpperBound(0, returnType)

        // We expect inner type to be something like Response<T> or maybe just T
        if (getRawType(flowInnerType) == Response::class.java) {
            // Case Flow<Response<T>>
            // Then inner type of Response<T>
            require(flowInnerType is ParameterizedType) { "Response must be parameterized" }
            val responseType = getParameterUpperBound(0, flowInnerType)
            return ResourceResponseAdapter<Any>(responseType)
        } else {
            // Case Flow<T>
            return ResourceBodyCallAdapter<Any>(flowInnerType)
        }
    }
}

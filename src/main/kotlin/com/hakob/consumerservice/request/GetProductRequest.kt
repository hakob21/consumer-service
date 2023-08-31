package com.hakob.consumerservice.request

import com.hakob.consumerservice.model.Product
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GetProductRequest(
    val restTemplate: RestTemplate = RestTemplate(),
    val baseUrl: String = "http://localhost"
) {
    val uri = "/product"
    val fullUrl = baseUrl + uri
    fun getProduct(): Product {
        val productResponseEntity = restTemplate.exchange(
            fullUrl,
            HttpMethod.GET,
            null,
            Product::class.java
        )
        return productResponseEntity.body!!
    }
}

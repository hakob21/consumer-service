package com.hakob.consumerservice.request

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.*
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import com.hakob.consumerservice.model.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.web.client.RestTemplateBuilder
import java.util.*

@ExtendWith(PactConsumerTestExt::class)
class GetProductRequestContractTest {
    @Pact(consumer = "consumer-service", provider = "provider-service")
    fun getAllProducts(builder: PactBuilder): V4Pact {
        return builder
            .given("products exist")
            .usingLegacyDsl()
            .uponReceiving("get all products")
            .method("GET")
            .path("/product")
            .willRespondWith()
            .status(200)
            .body(
                PactDslJsonBody()
                    .stringValue("productName", "product1")
                    .stringValue("productType", "productType1")
                    .stringValue("newField1", "newField1")
//                    .stringValue("newField2", "newField2")
                    .stringValue("newField3", "newField3")
                    .stringValue("newField4", "newField3")
            ).toPact(V4Pact::class.java)

    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    fun getAllProducts_whenProductsExist(mockServer: MockServer) {
        val expected = Product(productName = "product1")
        val restTemplate = RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .build()
        val products: Product = GetProductRequest(restTemplate, mockServer.getUrl()).getProduct()
        Assertions.assertEquals(expected, products)
    }
}
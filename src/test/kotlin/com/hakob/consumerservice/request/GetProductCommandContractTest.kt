package com.hakob.consumerservice.request

import au.com.dius.pact.consumer.MockServer
import au.com.dius.pact.consumer.dsl.*
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayMinLike
import au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.consumer.junit5.ProviderType
import au.com.dius.pact.core.model.RequestResponsePact
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import com.hakob.consumerservice.model.Product
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.web.client.RestTemplateBuilder
import java.util.*

@ExtendWith(PactConsumerTestExt::class)
class GetProductCommandContractTest {
    @Pact(consumer = "consumer-service", provider = "provider-service")
//    @PactTestFor(providerName = "provider-service", providerType = ProviderType.SYNCH)
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
//                "sdfas"

                PactDslJsonBody()
                    .stringValue("productName","product1")
                    .stringValue("productType", "productType")
//                newJsonObject {
//                    Product("product1")
//                }.asBody()
            ).toPact(V4Pact::class.java)
//            .expectsToReceiveHttpInteraction(
//                "fads"
//            ) { builder ->
//                builder
//                    .willRespondWith { it ->
//                        it.body("fads")
//                    }
//            }.toPact()

//            .uponReceiving("get all products")
//            .method("GET")
//            .path("/products")
////            .matchHeader(
////                "Authorization",
////                "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]"
////            )
//            .willRespondWith()
//            .status(200)
////            .headers(headers())
//            .body(
//                newJsonObject {
//                    "productName" to "product1"
//                }.asBody()
//            )
//            .toPact()
    }

    //mineeeee
//    fun getAllProducts(builder: PactDslWithProvider): RequestResponsePact {
//        return builder.given("products exist")
//            .uponReceiving("get all products")
//            .method("GET")
//            .path("/products")
////            .matchHeader(
////                "Authorization",
////                "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]"
////            )
//            .willRespondWith()
//            .status(200)
////            .headers(headers())
//            .body(
//                newJsonObject {
//                    "productName" to "product1"
//                }.asBody()
//            )
//            .toPact()
//    }


//    @Pact(consumer = "consumer-service", provider = "provider-service")
//    fun getAllProducts(builder: PactDslWithProvider): RequestResponsePact {
//        return builder.given("products exist")
//            .uponReceiving("get all products")
//            .method("GET")
//            .path("/products")
////            .matchHeader(
////                "Authorization",
////                "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]"
////            )
//            .willRespondWith()
//            .status(200)
////            .headers(headers())
////            .body(
////                newJsonArrayMinLike(
////                    2
////                ) { array ->
////                    array.`object` { `object` ->
////                        `object`.stringType("id", "09")
////                        `object`.stringType("type", "CREDIT_CARD")
////                        `object`.stringType("name", "Gem Visa")
////                    }
////                }.build()
////            )
//            .toPact()
//    }

    @Test
    @PactTestFor(pactMethod = "getAllProducts")
    fun getAllProducts_whenProductsExist(mockServer: MockServer) {
        val expected = Product(productName = "product1")
        val restTemplate = RestTemplateBuilder()
            .rootUri(mockServer.getUrl())
            .build()
        val products: Product = GetProductCommand(restTemplate, mockServer.getUrl()).getProduct()
        Assertions.assertEquals(expected, products)
    }
}
package com.colivery.serviceaping.repository

import com.colivery.serviceaping.dao.OrderDao
import com.colivery.serviceaping.repository.api.OrderRepository
import com.google.api.core.ApiFutureCallback
import com.google.api.core.ApiFutures
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QuerySnapshot
import com.google.common.util.concurrent.MoreExecutors
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class OrderFirestoreRepository(
        private val firestore: Firestore
) : OrderRepository {
    companion object {
        const val COLLECTION_NAME = "order"
    }

    override fun findAll() =
            Flux.create<OrderDao> { sink ->
                val future = this.firestore.collection(COLLECTION_NAME)
                        .get()

                ApiFutures.addCallback(future, object : ApiFutureCallback<QuerySnapshot> {
                    override fun onSuccess(result: QuerySnapshot) {
                        try {
                            result.toObjects(OrderDao::class.java)
                                    .forEach { sink.next(it) }
                        } catch (exception: RuntimeException) {
                            sink.error(exception)
                            exception.printStackTrace()
                        }

                        sink.complete()
                    }

                    override fun onFailure(t: Throwable?) {
                        t?.also { sink.error(it) }?.printStackTrace()
                    }

                }, MoreExecutors.directExecutor())
            }

    override fun findById(id: String): Mono<OrderDao> {
        TODO("Not yet implemented")
    }

    override fun create(element: OrderDao) {
        TODO("Not yet implemented")
    }

    override fun update(element: OrderDao) {
        TODO("Not yet implemented")
    }

    override fun delete(element: OrderDao) {
        TODO("Not yet implemented")
    }
}

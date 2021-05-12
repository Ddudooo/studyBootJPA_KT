package study.jpashopkt.repo

import org.springframework.stereotype.Repository
import study.jpashopkt.domain.Order
import javax.persistence.EntityManager

@Repository
class OrderRepo(
    val em: EntityManager
) {
    fun save(order: Order) {
        em.persist(order)
    }

    fun findOne(orderId: Long): Order {
        return em.find(Order::class.java, orderId)
    }
}
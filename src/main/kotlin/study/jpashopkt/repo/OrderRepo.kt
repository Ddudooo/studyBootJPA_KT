package study.jpashopkt.repo

import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import study.jpashopkt.domain.Order
import study.jpashopkt.dto.OrderSearch
import study.jpashopkt.dto.SimpleOrderDto
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

    fun findAll(orderSearch: OrderSearch): List<Order> {
        var query = "select o from Order o join o.member m "

        var isFirstCondition: Boolean = true
        if (orderSearch.orderStatus != null) {
            if (isFirstCondition) {
                query += " where"
                isFirstCondition = false
            } else {
                query += " and"
            }
            query += " o.status = :status"
        }

        if (StringUtils.hasText(orderSearch.memberName)) {
            if (isFirstCondition) {
                query += " where"
                isFirstCondition = false
            } else {
                query += " and"
            }
            query += " m.name like :name"
        }

        var resultQuery = em.createQuery(
            query, Order::class.java
        )
            .setMaxResults(1000)

        if (orderSearch.orderStatus != null) {
            resultQuery = resultQuery.setParameter("status", orderSearch.orderStatus)
        }
        if (StringUtils.hasText(orderSearch.memberName)) {
            resultQuery = resultQuery.setParameter("name", orderSearch.memberName)
        }

        return resultQuery.resultList
    }

    fun findAllWithMemberDelivery(): List<Order> {
        return em.createQuery(
            "select o From Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d",
            Order::class.java
        )
            .resultList
    }

    fun findOrderDtos(): List<SimpleOrderDto> {
        return em.createQuery(
            "select new study.jpashopkt.dto.SimpleOrderDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                    "from Order o " +
                    "join o.member m " +
                    "join o.delivery d ", SimpleOrderDto::class.java
        )
            .resultList
    }

    fun findAllWithItem(): MutableList<Order> {
        return em.createQuery(
            "select distinct o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery " +
                    "join fetch o.orderItems oi " +
                    "join fetch oi.item i", Order::class.java
        )
            .resultList ?: mutableListOf()
    }

    fun findAllWithMemberDelivery(offset: Int, limit: Int): List<Order> {
        return em.createQuery(
            "select o From Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d", Order::class.java
        )
            .setFirstResult(offset)
            .setMaxResults(limit)
            .resultList
    }
}
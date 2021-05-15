package study.jpashopkt.repo.query

import org.springframework.stereotype.Repository
import study.jpashopkt.dto.OrderFlatDto
import study.jpashopkt.dto.OrderItemQueryDto
import study.jpashopkt.dto.OrderQueryDto
import java.util.stream.Collectors
import javax.persistence.EntityManager

@Repository
class OrderQueryRepo(
    private val em: EntityManager
) {
    fun findOrderQueryDtos(): List<OrderQueryDto> {
        val result = findOrders()

        result.forEach {
            val orderItems: List<OrderItemQueryDto> = findOrderItems(it.orderId)
            it.orderItems = orderItems
        }
        return result
    }

    private fun findOrderItems(orderId: Long): List<OrderItemQueryDto> {
        return em.createQuery(
            "select " +
                    "new study.jpashopkt.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                    "from OrderItem oi " +
                    "join oi.item i " +
                    "where oi.order.id = :orderId", OrderItemQueryDto::class.java
        )
            .setParameter("orderId", orderId)
            .resultList
    }

    private fun findOrders() = em.createQuery(
        "select " +
                "new study.jpashopkt.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderQueryDto::class.java
    )
        .resultList

    fun findAllByDtoWithOptimize(): List<OrderQueryDto> {
        val result = findOrders()

        val orderItemMap = findOrderItemMap(toOrderIds(result))

        result.forEach { it.orderItems = orderItemMap[it.orderId] ?: listOf() }

        return result
    }

    private fun findOrderItemMap(orderIds: List<Long>): Map<Long, MutableList<OrderItemQueryDto>> {
        val orderItems = em.createQuery(
            "select " +
                    "new study.jpashopkt.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                    "from OrderItem oi " +
                    "join oi.item i " +
                    "where oi.order.id in :orderIds", OrderItemQueryDto::class.java
        )
            .setParameter("orderIds", orderIds)
            .resultList

        val orderItemMap = orderItems.stream()
            .collect(Collectors.groupingBy { it.orderId })
        return orderItemMap
    }

    private fun toOrderIds(result: MutableList<OrderQueryDto>): List<Long> {
        val orderIds: List<Long> = result.stream()
            .map { it.orderId }
            .collect(Collectors.toList())
        return orderIds
    }

    fun findAllByDtoFlat(): List<OrderFlatDto> {
        return em.createQuery(
            "" +
                    "select new study.jpashopkt.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) " +
                    "from Order o " +
                    "join o.member m " +
                    "join o.delivery d " +
                    "join o.orderItems oi " +
                    "join oi.item i", OrderFlatDto::class.java
        ).resultList
    }
}
package study.jpashopkt.dto

import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Order
import study.jpashopkt.domain.OrderStatus
import java.time.LocalDateTime

data class SimpleOrderDto(
    val orderId: Long,
    val name: String,
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus,
    val address: Address
) {
    constructor (order: Order) : this(
        order.id!!,
        order.member.name,
        order.orderDate,
        order.status,
        order.delivery.address
    )
}
package study.jpashopkt.dto

import study.jpashopkt.domain.Address
import study.jpashopkt.domain.OrderStatus
import java.time.LocalDateTime

data class OrderFlatDto(
    val orderId: Long,
    val name: String,
    val orderDate: LocalDateTime,
    val orderStatus: OrderStatus,
    val address: Address,
    val itemName: String,
    val orderPrice: Int,
    val count: Int
) {

}
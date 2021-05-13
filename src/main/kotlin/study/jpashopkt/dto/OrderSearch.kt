package study.jpashopkt.dto

import study.jpashopkt.domain.OrderStatus

data class OrderSearch(
    var memberName: String = "",
    var orderStatus: OrderStatus? = null
)
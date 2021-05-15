package study.jpashopkt.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Order
import study.jpashopkt.domain.OrderItem
import study.jpashopkt.domain.OrderStatus
import study.jpashopkt.dto.OrderFlatDto
import study.jpashopkt.dto.OrderQueryDto
import study.jpashopkt.dto.OrderSearch
import study.jpashopkt.repo.OrderRepo
import study.jpashopkt.repo.query.OrderQueryRepo
import java.time.LocalDateTime
import java.util.stream.Collectors

@RestController
class OrderApi(
    private val orderRepo: OrderRepo,
    private val orderQueryRepo: OrderQueryRepo
) {

    /*@GetMapping("/api/v1/orders")
    fun ordersV1(): List<Order> {
        val findAll = orderRepo.findAll(OrderSearch())
        for (order in findAll) {
            order.member.name
            order.delivery.address
            for (item in order.orderItems) {
                item.item.name
            }
        }
        return findAll
    }*/

    @GetMapping("/api/v2/orders")
    fun ordersV2(): List<OrderDto> {
        val findAll = orderRepo.findAll(OrderSearch())
        val dtoList = findAll.stream()
            .map { OrderDto(it) }
            .collect(Collectors.toList())
        return dtoList
    }

    @GetMapping("/api/v3/orders")
    fun orderV3(): List<OrderDto> {
        val orders = orderRepo.findAllWithItem()
        val dtoList = orders.stream()
            .map { OrderDto(it) }
            .collect(Collectors.toList())
        return dtoList
    }

    @GetMapping("/api/v3.1/orders")
    fun orderV3_page(
        @RequestParam(value = "offset", defaultValue = "0") offset: Int,
        @RequestParam(value = "limit", defaultValue = "1") limit: Int
    ): List<OrderDto> {
        val orders = orderRepo.findAllWithMemberDelivery(offset, limit)
        val dtoList = orders.stream()
            .map { OrderDto(it) }
            .collect(Collectors.toList())
        return dtoList
    }

    @GetMapping("/api/v4/orders")
    fun ordersV4(): List<OrderQueryDto> {
        return orderQueryRepo.findOrderQueryDtos()
    }

    @GetMapping("/api/v5/orders")
    fun orderV5(): List<OrderQueryDto> = orderQueryRepo.findAllByDtoWithOptimize()

    @GetMapping("/api/v6/orders")
    fun orderV6(): List<OrderFlatDto> = orderQueryRepo.findAllByDtoFlat()

    data class OrderDto(
        val orderId: Long,
        val name: String,
        val orderDate: LocalDateTime,
        val orderStatus: OrderStatus,
        val address: Address,
        val orderItems: MutableList<OrderItemDto>
    ) {
        constructor(order: Order) : this(
            order.id!!,
            order.member.name,
            order.orderDate,
            order.status,
            order.delivery.address,
            order.orderItems.stream()
                .map { OrderItemDto(it) }
                .collect(Collectors.toList())
        )

        data class OrderItemDto(
            val itemId: Long,
            val name: String,
            val price: Int,
            val quantity: Int
        ) {
            constructor(orderItem: OrderItem) : this(
                orderItem.id!!,
                orderItem.item.name,
                orderItem.orderPrice,
                orderItem.count
            )
        }
    }

}
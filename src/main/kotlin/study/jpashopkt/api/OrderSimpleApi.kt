package study.jpashopkt.api

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import study.jpashopkt.domain.Order
import study.jpashopkt.dto.OrderSearch
import study.jpashopkt.dto.SimpleOrderDto
import study.jpashopkt.repo.OrderRepo
import java.util.stream.Collectors

private val log = KotlinLogging.logger {}

@RestController
class OrderSimpleApi(
    private val orderRepo: OrderRepo
) {
    /*@GetMapping("/api/v1/simple-orders")
    fun ordersV1(): List<Order> {
        val orders = orderRepo.findAll(OrderSearch())
        for (order in orders) {
            order.member.name
            order.delivery.address
            for (orderItem in order.orderItems) {
                orderItem.item.name
            }
        }
        return orders
    }*/

    @GetMapping("/api/v2/simple-orders")
    fun ordersV2(): List<SimpleOrderDto> {
        val orders = orderRepo.findAll(OrderSearch())
        val dtoList = orders.stream()
            .map { SimpleOrderDto(it) }
            .collect(Collectors.toList())
        log.info("dto size ${dtoList.size}")
        return dtoList
    }

    @GetMapping("/api/v3/simple-orders")
    fun ordersV3(): List<SimpleOrderDto> {
        val orders: List<Order> = orderRepo.findAllWithMemberDelivery()
        return orders.stream()
            .map { SimpleOrderDto(it) }
            .collect(Collectors.toList())
    }

    @GetMapping("/api/v4/simple-orders")
    fun ordersV4(): List<SimpleOrderDto> {
        return orderRepo.findOrderDtos()
    }
}
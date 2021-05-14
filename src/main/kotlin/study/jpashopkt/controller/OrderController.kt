package study.jpashopkt.controller

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import study.jpashopkt.domain.Member
import study.jpashopkt.domain.Order
import study.jpashopkt.domain.item.Item
import study.jpashopkt.dto.OrderSearch
import study.jpashopkt.service.ItemService
import study.jpashopkt.service.MemberService
import study.jpashopkt.service.OrderService

private val log = KotlinLogging.logger { }


@Controller
class OrderController(
    val orderService: OrderService,
    val memberService: MemberService,
    val itemService: ItemService
) {
    @GetMapping("/order")
    fun createForm(model: Model): String {
        val members: List<Member> = memberService.findMembers()
        val items: List<Item> = itemService.findItems()
        model.addAttribute("members", members)
        model.addAttribute("items", items)

        return "order/orderForm"
    }

    @PostMapping("/order")
    fun order(
        @RequestParam("memberId") memberId: Long,
        @RequestParam("itemId") itemId: Long,
        @RequestParam("count") count: Int
    ): String {
        val orderId: Long = orderService.order(memberId, itemId, count)
        return "redirect:/orders/${orderId}"
    }

    @GetMapping("/orders")
    fun orderList(
        @ModelAttribute("orderSearch") orderSearch: OrderSearch,
        model: Model
    ): String {
        val orders: List<Order> = orderService.findOrders(orderSearch)
        model.addAttribute("orders", orders)
        log.info("orders size ${orders.size}")
        for (order in orders) {
            log.info("order ${order.orderItems.size}")
            for (orderItem in order.orderItems)
                log.info("          $orderItem")
        }
        return "order/orderList"
    }

    @PostMapping("/orders/{orderId}/cancel")
    fun cancelOrder(@PathVariable("orderId") orderId: Long): String {
        orderService.cancelOrder(orderId)
        return "redirect:/orders"
    }
}
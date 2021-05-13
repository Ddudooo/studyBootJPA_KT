package study.jpashopkt.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.jpashopkt.domain.Delivery
import study.jpashopkt.domain.Member
import study.jpashopkt.domain.Order
import study.jpashopkt.domain.OrderItem
import study.jpashopkt.domain.createOrder
import study.jpashopkt.domain.createOrderItem
import study.jpashopkt.domain.item.Item
import study.jpashopkt.dto.OrderSearch
import study.jpashopkt.repo.ItemRepo
import study.jpashopkt.repo.MemberRepo
import study.jpashopkt.repo.OrderRepo

@Service
@Transactional(readOnly = true)
class OrderService(
    private val orderRepo: OrderRepo,
    private val memberRepo: MemberRepo,
    private val itemRepo: ItemRepo
) {
    @Transactional
    fun order(memberId: Long, itemId: Long, count: Int): Long {
        //엔티티 조회
        val member: Member = memberRepo.findOne(memberId)
        val item: Item = itemRepo.findOne(itemId)

        //배송정보 생성
        val delivery: Delivery = Delivery(address = member.address)

        //주문 상품 생성
        val createdOrderItem: OrderItem = createOrderItem(item, item.price, count)

        //주문 생성
        val createOrder = createOrder(member, delivery, createdOrderItem)
        createdOrderItem.order = createOrder

        //주문 저장
        orderRepo.save(createOrder)

        return createOrder.id!!
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val findOrder: Order = orderRepo.findOne(orderId)
        findOrder.cancel()
    }

    fun findOrders(orderSearch: OrderSearch): List<Order> {
        return orderRepo.findAll(orderSearch)
    }
}
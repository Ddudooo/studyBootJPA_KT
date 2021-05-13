package study.jpashopkt.domain

import study.jpashopkt.domain.item.Item
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class OrderItem(
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    var item: Item,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "order_id")
    var order: Order? = null,

    var orderPrice: Int = 0,
    var count: Int = 0
) {
    fun cancel() {
        item.addStock(count)
    }

    fun getTotalPrice(): Int {
        return this.orderPrice * this.count
    }
}

fun createOrderItem(item: Item, orderPrice: Int, count: Int): OrderItem {
    val orderItem: OrderItem = OrderItem(item = item, orderPrice = orderPrice, count = count)
    item.removeStock(count)
    return orderItem
}

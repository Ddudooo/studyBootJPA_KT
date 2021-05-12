package study.jpashopkt.domain.item

import study.jpashopkt.domain.Category
import study.jpashopkt.exception.NotEnoughStockException
import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.ManyToMany

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item(
    open var name: String,
    open var price: Int,
    open var stockQuantity: Int
) {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    open var id: Long? = null

    @ManyToMany(mappedBy = "items")
    open var categories: MutableList<Category> = mutableListOf()

    /**
     * stock 증가
     */
    fun addStock(quantity: Int) {
        this.stockQuantity += quantity
    }

    /**
     * stock 감소
     */
    fun removeStock(quantity: Int) {
        val restStock: Int = this.stockQuantity - quantity
        if (restStock < 0) {
            throw NotEnoughStockException("need more stock")
        }
        this.stockQuantity = restStock
    }
}

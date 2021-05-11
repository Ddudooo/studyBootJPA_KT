package study.jpashopkt.domain.item

import javax.persistence.Column
import javax.persistence.DiscriminatorColumn
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Inheritance
import javax.persistence.InheritanceType

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item(
    var name: String,
    var price: Int,
    var stockQuantity: Int
) {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null
}

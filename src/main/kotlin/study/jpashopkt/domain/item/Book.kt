package study.jpashopkt.domain.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "B")
class Book(
    var author: String,
    var isbn: String,
    name: String, price: Int, stockQuantity: Int
) : Item(name, price, stockQuantity)
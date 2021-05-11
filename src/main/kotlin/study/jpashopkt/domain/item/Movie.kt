package study.jpashopkt.domain.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "M")
class Movie(
    var director: String,
    var actor: String,
    name: String, price: Int, stockQuantity: Int
) : Item(name, price, stockQuantity)
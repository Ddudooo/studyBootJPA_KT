package study.jpashopkt.domain.item

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue(value = "A")
class Album(
    var artist: String,
    var etc: String,
    name: String, price: Int, stockQuantity: Int
) : Item(name, price, stockQuantity)
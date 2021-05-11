package study.jpashopkt.domain

import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Member(
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    var id: Long? = null,

    var name: String,

    @Embedded
    var address: Address,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    var orders: MutableList<Order> = mutableListOf()
)

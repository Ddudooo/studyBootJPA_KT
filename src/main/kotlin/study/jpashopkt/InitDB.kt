package study.jpashopkt

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Delivery
import study.jpashopkt.domain.Member
import study.jpashopkt.domain.createOrder
import study.jpashopkt.domain.createOrderItem
import study.jpashopkt.domain.item.Book
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Component
class InitDB(
    private val initService: InitService
) {

    @PostConstruct
    fun init() {
        initService.dbInit1()
        initService.dbInit2()
    }

    @Component
    @Transactional
    class InitService(
        private val em: EntityManager
    ) {
        fun dbInit1() {
            val member1 = Member(name = "userA", address = Address("서울", "1", "111111"))
            em.persist(member1)

            val book1 = Book("김영한", "isbn", "JPA1 BOOK", 10000, 100)
            em.persist(book1)

            val book2 = Book("김영한", "isbn", "JPA2 BOOK", 20000, 100)
            em.persist(book2)

            val orderItem1 = createOrderItem(book1, 10000, 1)
            val orderItem2 = createOrderItem(book2, 20000, 2)


            val delivery: Delivery = Delivery(address = member1.address)
            val order = createOrder(member1, delivery, orderItem1, orderItem2)
            em.persist(order)
        }

        fun dbInit2() {
            val member2 = Member(name = "userB", address = Address("경기", "2", "222222"))
            em.persist(member2)

            val book1 = Book("김영한", "isbn", "SPRING1 BOOK", 20000, 200)
            em.persist(book1)

            val book2 = Book("김영한", "isbn", "SPRING2 BOOK", 40000, 200)
            em.persist(book2)

            val orderItem1 = createOrderItem(book1, 20000, 3)
            val orderItem2 = createOrderItem(book2, 40000, 4)

            val delivery: Delivery = Delivery(address = member2.address)
            val order = createOrder(member2, delivery, orderItem1, orderItem2)
            em.persist(order)
        }
    }
}
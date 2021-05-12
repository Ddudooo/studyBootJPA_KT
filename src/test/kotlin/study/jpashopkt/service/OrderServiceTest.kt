package study.jpashopkt.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Member
import study.jpashopkt.domain.Order
import study.jpashopkt.domain.OrderStatus
import study.jpashopkt.domain.item.Book
import study.jpashopkt.domain.item.Item
import study.jpashopkt.exception.NotEnoughStockException
import study.jpashopkt.repo.OrderRepo
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
internal class OrderServiceTest(
    @Autowired
    val orderService: OrderService,
    @Autowired
    val orderRepo: OrderRepo,
    @Autowired
    val em: EntityManager
) {

    @Test
    @DisplayName("상품 주문 테스트")
    fun orderTest() {
        //given
        val member: Member = createMember("회원")
        val book: Book = createBook("김영한 선생님", "JPA", 10000, 10)

        //when
        val orderId = orderService.order(member.id!!, book.id!!, 2)

        //then
        val findOrder: Order = orderRepo.findOne(orderId)

        assertThat(findOrder.status).isEqualTo(OrderStatus.ORDER)
        assertThat(findOrder.orderItems.size).isEqualTo(1)
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000 * 2)
        assertThat(book.stockQuantity).isEqualTo(8)
    }

    private fun createBook(author: String, name: String, price: Int, stock: Int): Book {
        val book: Book = Book(author, "아무거나", name, price, stock)
        em.persist(book)
        return book
    }

    private fun createMember(name: String): Member {
        val address: Address = Address("서울", "아무로", "123-123")
        val member: Member = Member(name = name, address = address)
        em.persist(member)
        return member
    }

    @Test
    @DisplayName("재고 수량 부족시 오류 발생 테스트")
    fun stockQuantityNotEnoughExceptionTest() {
        //given
        val member: Member = createMember("무야호")
        val item: Item = createBook("홍길동", "홍길동전", 10000, 10)

        //when
        //then
        val assertThrows = assertThrows(NotEnoughStockException::class.java) {
            orderService.order(
                member.id!!,
                item.id!!,
                11
            )
        }
        assertThat(assertThrows.message).isEqualTo("need more stock")
    }

    @Test
    @DisplayName("주문 취소 테스트")
    fun orderCancelTest() {
        //given
        val createMember = createMember("회원")
        val item: Book = createBook("아무개", "책", 10000, 10)

        val orderId: Long = orderService.order(createMember.id!!, item.id!!, 2)
        //when
        orderService.cancelOrder(orderId)
        //then
        val findOrder: Order = orderRepo.findOne(orderId)
        assertThat(findOrder.status).isEqualTo(OrderStatus.CANCEL)
        assertThat(item.stockQuantity).isEqualTo(10)
    }
}
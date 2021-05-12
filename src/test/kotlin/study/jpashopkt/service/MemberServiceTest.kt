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
import study.jpashopkt.repo.MemberRepo
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
internal class MemberServiceTest(
    @Autowired
    val memberService: MemberService,
    @Autowired
    val memberRepo: MemberRepo,
    @Autowired
    val em: EntityManager
) {

    @Test
    @DisplayName("회원가입 테스트")
    fun joinTest() {
        //given
        val memberAddress: Address = Address("city", "street", "zipcode")
        val member: Member = Member(name = "newMember", address = memberAddress)

        //when
        val savedId: Long = memberService.join(member)

        //then
        em.flush()
        //assertThat(savedId).isEqualTo(member.id)
        assertThat(member).isEqualTo(memberRepo.findOne(savedId))
    }

    @Test
    @DisplayName("중복 회원 예외 테스트")
    fun validateJoinMemberTest() {
        //given
        val address: Address = Address("city", "street", "zipcode")
        val member1: Member = Member(name = "홍길동", address = address)
        val member2: Member = Member(name = "홍길동", address = address)

        //when
        memberService.join(member1)
        //memberService.join(member2)

        //then
        val assertThrows =
            assertThrows(IllegalStateException::class.java) { memberService.join(member2) }
        assertThat(assertThrows.message).isEqualTo("이미 존재하는 회원입니다.")
    }
}
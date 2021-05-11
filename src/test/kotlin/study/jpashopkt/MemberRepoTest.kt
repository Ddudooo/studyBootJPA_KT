package study.jpashopkt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class MemberRepoTest @Autowired constructor(
    val memberRepo: MemberRepo
){
    @Test
    @Transactional
//    @Rollback(false)
    fun testMethod() {
        //given
        val member = Member("username")

        //when
        val savedId = memberRepo.save(member)
        val findMember = memberRepo.find(savedId)

        //then
        assertThat(findMember.id).isEqualTo(member.id)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember).isEqualTo(member)
    }
}
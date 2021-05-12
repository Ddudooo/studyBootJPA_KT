package study.jpashopkt.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.jpashopkt.domain.Member
import study.jpashopkt.repo.MemberRepo

@Service
@Transactional(readOnly = true)
class MemberService(
    val memberRepo: MemberRepo
) {
    @Transactional
    fun join(member: Member): Long {
        validateDuplicateMember(member)
        memberRepo.save(member)
        return member.id!!
    }

    private fun validateDuplicateMember(member: Member) {
        val findMembers: List<Member> = memberRepo.findByName(member.name)
        if (!findMembers.isNullOrEmpty()) {
            throw IllegalStateException("이미 존재하는 회원입니다.")
        }
    }

    fun findMembers(): List<Member> {
        return memberRepo.findAll()
    }

    fun findOne(memberId: Long): Member {
        return memberRepo.findOne(memberId)
    }
}
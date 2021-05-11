package study.jpashopkt

import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class MemberRepo(
    @PersistenceContext
    val em: EntityManager
){
    fun save(member: Member):Long {
        em.persist(member)
        return member.id?:throw RuntimeException("fail to save member")
    }

    fun find(id : Long):Member {
        return em.find(Member::class.java, id)
    }
}
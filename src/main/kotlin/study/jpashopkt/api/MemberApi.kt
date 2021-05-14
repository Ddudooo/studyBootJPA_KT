package study.jpashopkt.api

import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Member
import study.jpashopkt.service.MemberService
import java.util.stream.Collectors
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

private val log = KotlinLogging.logger { }

@RestController
class MemberApi(
    private val memberService: MemberService
) {

    @GetMapping("/api/v1/members")
    fun memberV1(): List<Member> {
        log.info("get member list")
        return memberService.findMembers()
    }

    @GetMapping("/api/v2/members")
    fun memberV2(): Result<List<MemberDto>> {
        val findMembers = memberService.findMembers()
        log.info("find members size = ${findMembers.size}")
        val dtos = findMembers.stream()
            .map { m -> MemberDto(m.name) }
            .collect(Collectors.toList())
        log.info("dto list size = ${dtos.size}")
        return Result(dtos)
    }

    data class Result<T>(
        val data: T
    )

    data class MemberDto(
        val name: String
    )

    @PostMapping("/api/v1/members")
    fun saveMemberV1(
        @RequestBody @Valid member: Member
    ): CreateMemberResponse {
        log.info("save member v1 ${member.name}")
        val id: Long = memberService.join(member)
        return CreateMemberResponse(id)
    }

    @PostMapping("/api/v2/members")
    fun saveMemberV2(
        @RequestBody @Valid request: CreateMemberRequest
    ): CreateMemberResponse {
        log.info("save member v2 $request")
        val member: Member = Member(
            name = request.name,
            address = Address(request.city, request.street, request.zipcode)
        )
        val id: Long = memberService.join(member)
        return CreateMemberResponse(id)
    }

    @PutMapping("/api/v2/members/{id}")
    fun updateMemberV2(
        @PathVariable("id") id: Long,
        @RequestBody @Valid request: UpdateMemberRequest
    ): UpdateMemberResponse {
        memberService.update(id, request.name)
        val updatedMember: Member = memberService.findOne(id)
        return UpdateMemberResponse(updatedMember.id!!, updatedMember.name)
    }

    data class UpdateMemberRequest(
        val name: String
    )

    data class UpdateMemberResponse(
        val id: Long,
        val name: String
    )

    data class CreateMemberRequest(
        @NotEmpty
        val name: String,
        val city: String,
        val street: String,
        val zipcode: String
    )

    data class CreateMemberResponse(
        val id: Long
    )
}
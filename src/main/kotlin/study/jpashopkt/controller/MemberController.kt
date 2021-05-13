package study.jpashopkt.controller

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import study.jpashopkt.domain.Address
import study.jpashopkt.domain.Member
import study.jpashopkt.dto.MemberForm
import study.jpashopkt.service.MemberService
import javax.validation.Valid

private val log = KotlinLogging.logger {}

@Controller
class MemberController(
    val memberService: MemberService
) {
    @GetMapping("/members/new")
    fun createForm(model: Model): String {
        model.addAttribute("memberForm", MemberForm())
        return "members/createMemberForm"
    }

    @PostMapping("/members/new")
    fun create(@Valid memberForm: MemberForm, result: BindingResult): String {

        if (result.hasErrors()) {
            result.allErrors.forEach { log.warn("${it.arguments} ${it.defaultMessage}") }
            return "members/createMemberForm"
        }

        val address: Address = Address(memberForm.city, memberForm.street, memberForm.zipcode)
        val member: Member = Member(name = memberForm.name, address = address)
        memberService.join(member)
        return "redirect:/"
    }

    @GetMapping("/members")
    fun list(model: Model): String {
        val members: List<Member> = memberService.findMembers()
        model.addAttribute("members", members)
        return "members/memberList"
    }
}
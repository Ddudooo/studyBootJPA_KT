package study.jpashopkt.controller

import mu.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

private val log = KotlinLogging.logger {}

@Controller
class HomeController {

    @GetMapping("/")
    fun home(): String {
        log.info("home controller")
        return "home"
    }
}
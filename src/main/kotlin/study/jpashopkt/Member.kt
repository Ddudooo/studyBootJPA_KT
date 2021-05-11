package study.jpashopkt

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Member {
    @Id
    @GeneratedValue
    var id : Long? = null

    var username : String

    constructor(username: String){
        this.username = username
    }
}
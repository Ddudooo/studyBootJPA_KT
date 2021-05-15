package study.jpashopkt.dto

data class OrderItemQueryDto(
    val orderId: Long,
    val itemName: String,
    val orderPrice: Int,
    val count: Int
) {

}

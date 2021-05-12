package study.jpashopkt.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import study.jpashopkt.domain.item.Item
import study.jpashopkt.repo.ItemRepo

@Service
@Transactional(readOnly = true)
class ItemService(
    val itemRepo: ItemRepo
) {

    @Transactional
    fun saveItem(item: Item) {
        itemRepo.save(item)
    }

    fun findItems(): List<Item> {
        return itemRepo.findAll()
    }

    fun findOne(itemId: Long): Item {
        return itemRepo.findOne(itemId)
    }
}
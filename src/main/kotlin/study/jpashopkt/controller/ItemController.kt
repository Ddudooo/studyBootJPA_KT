package study.jpashopkt.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import study.jpashopkt.domain.item.Book
import study.jpashopkt.domain.item.Item
import study.jpashopkt.dto.BookForm
import study.jpashopkt.service.ItemService

@Controller
class ItemController(
    val itemService: ItemService
) {

    @GetMapping("/items/new")
    fun createForm(model: Model): String {
        model.addAttribute("form", BookForm())
        return "items/createItemForm"
    }

    @PostMapping("/items/new")
    fun crate(form: BookForm): String {
        val book: Book = Book(form.author, form.isbn, form.name, form.price, form.stockQuantity)
        itemService.saveItem(book)
        return "redirect:/"
    }

    @GetMapping("/items")
    fun list(model: Model): String {
        val items: List<Item> = itemService.findItems()
        model.addAttribute("items", items)
        return "items/itemList"
    }

    @GetMapping("items/{itemId}/edit")
    fun updateItemForm(
        @PathVariable("itemId") itemId: Long,
        model: Model
    ): String {
        val item: Book = itemService.findOne(itemId) as Book
        val form: BookForm =
            BookForm(item.id, item.name, item.price, item.stockQuantity, item.author, item.isbn)

        model.addAttribute("form", form)
        return "items/updateItemForm"
    }

    @PostMapping("items/{itemId}/edit")
    fun updateItem(@ModelAttribute("form") form: BookForm): String {
        itemService.updateItem(form.id!!, form.name, form.price, form.stockQuantity)
        return "redirect:/items"
    }
}
package com.alik.project2withboot.controllers;

import com.alik.project2withboot.models.Book;
import com.alik.project2withboot.models.Person;
import com.alik.project2withboot.services.BooksService;
import com.alik.project2withboot.services.PeopleService;
import com.alik.project2withboot.util.BookValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/books")
public class BookController {
    BooksService booksService;
    PeopleService peopleService;
    BookValidator bookValidator;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer books_per_page,
                        @RequestParam(name = "sort_by_year", required = false) Boolean sort_by_year) {
        model.addAttribute("books", booksService.index(page, books_per_page, sort_by_year));
        return "/books/index";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int book_id,
                       Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.show(book_id));
        model.addAttribute("people", peopleService.index());
        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{book_id}/edit")
    public String edit(Model model, @PathVariable("book_id") int book_id) {
        model.addAttribute("book", booksService.show(book_id));
        return "books/edit";
    }

    @PatchMapping("/{book_id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult, @PathVariable("book_id") int book_id) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        booksService.update(book_id, book);
        return "redirect:/books/{book_id}";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int book_id) {
        booksService.delete(book_id);
        return "redirect:/books";
    }

    @PatchMapping("/{book_id}/addOwner")
    public String addOwner(@PathVariable("book_id") int book_id,
                           @ModelAttribute("person") Person person) {
        booksService.addPersonToBook(book_id, person.getPerson_id());
        return "redirect:/books/{book_id}";
    }

    @PatchMapping("/{book_id}/releaseBook")
    public String releaseBook(@PathVariable("book_id") int book_id) {
        booksService.releaseBook(book_id);
        return "redirect:/books/{book_id}";
    }

    @GetMapping("/search")
    public String search() {
        return "books/search";
    }

    @PostMapping("/search")
    public String find(Model model, @RequestParam("keywords") String keywords) {
        model.addAttribute("foundBooks",booksService.findByNameStartingWith(keywords));
        return "books/search";
    }

}

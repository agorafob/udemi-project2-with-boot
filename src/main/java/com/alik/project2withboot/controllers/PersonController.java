package com.alik.project2withboot.controllers;


import com.alik.project2withboot.models.Book;
import com.alik.project2withboot.models.Person;
import com.alik.project2withboot.services.BooksService;
import com.alik.project2withboot.services.PeopleService;
import com.alik.project2withboot.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/people")
public class PersonController {
    PeopleService peopleService;
    BooksService booksService;
    PersonValidator personValidator;

    @Autowired
    public PersonController(PeopleService peopleService, BooksService booksService,PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.index());
        return "/people/index";
    }

    @GetMapping("/{person_id}")
    public String show(@PathVariable("person_id") int person_id, Model model,
                       @ModelAttribute("book") Book book) {
        model.addAttribute("person", peopleService.show(person_id));
        return "/people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "/people/new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{person_id}/edit")
    public String edit(Model model, @PathVariable("person_id") int person_id) {
        model.addAttribute("person", peopleService.show(person_id));
        return "people/edit";
    }

    @PatchMapping("/{person_id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult, @PathVariable("person_id") int person_id) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/people/edit";
        }
        peopleService.update(person_id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{person_id}")
    public String delete(@PathVariable("person_id") int person_id) {
        peopleService.delete(person_id);
        return "redirect:/people";
    }
}

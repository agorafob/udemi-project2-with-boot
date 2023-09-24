package com.alik.project2withboot.util;


import com.alik.project2withboot.models.Book;
import com.alik.project2withboot.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class BookValidator implements Validator {
   private final BooksService booksService;


    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (Objects.nonNull(booksService.show(book.getName())) &&
                booksService.show(book.getBook_id()).getBook_id() != book.getBook_id()) {
            errors.rejectValue("name", "", "Name should be unique");
        }
    }
}

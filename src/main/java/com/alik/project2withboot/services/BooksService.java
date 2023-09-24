package com.alik.project2withboot.services;


import com.alik.project2withboot.models.Book;
import com.alik.project2withboot.models.Person;
import com.alik.project2withboot.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> index(Integer page, Integer books_per_page, Boolean sort_by_year) {
        List<Book> books;
        if (Objects.nonNull(page) && Objects.nonNull(books_per_page) && Objects.nonNull(sort_by_year)) {
            if(sort_by_year){
                books = findByPageAndSort(page, books_per_page);
            }else {
                books = findByPage(page, books_per_page);
            }
        } else if (Objects.nonNull(page) && Objects.nonNull(books_per_page)) {
            books = findByPage(page, books_per_page);
        }else {
            books=booksRepository.findAll();
        }
        return books;
    }

    public Book show(int book_id) {
        return booksRepository.findById(book_id).orElse(null);
    }

    public Book show(String name) {
        return booksRepository.findByName(name).orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int book_id, Book updateBook) {
        updateBook.setBook_id(book_id);
        updateBook.setOwner(show(book_id).getOwner());
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int book_id) {
        booksRepository.deleteById(book_id);
    }


    @Transactional
    public void addPersonToBook(int book_id, int person_id) {
        Book book = show(book_id);
        Person person = peopleService.show(person_id);
        if (Objects.nonNull(book)) {
            book.setOwner(person);
            book.setBookAssigned(new Date());
//            booksRepository.save(book);
        }
    }

    @Transactional
    public void releaseBook(int book_id) {
        Book book = show(book_id);
        if (Objects.nonNull(book)) {
            book.setOwner(null);
            book.setBookAssigned(null);
//            booksRepository.save(book);
        }
    }

    private List<Book> findByPageAndSort(int pageNumber, int sizeOnPage) {
        PageRequest pr = PageRequest.of(pageNumber, sizeOnPage, Sort.by("year"));
        return booksRepository.findAll(pr).getContent();
    }

    private List<Book> findByPage(int pageNumber, int sizeOnPage) {
        PageRequest pr = PageRequest.of(pageNumber, sizeOnPage);
        return booksRepository.findAll(pr).getContent();
    }

    public List<Book> findByNameStartingWith(String keywords){
        return booksRepository.findByNameStartingWith(keywords);
    }
}

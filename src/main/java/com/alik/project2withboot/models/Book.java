package com.alik.project2withboot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int book_id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, message = "Name should be min 2 characters")
    @Pattern(regexp = "[A-Z]\\w* [d\\w\\s\\p{P}\\p{S}]*", message = "First letter should be Upper Case and then any word or symbol")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Pattern(regexp = "[A-Z]\\w* [A-Z][d\\w\\s]*", message = "Words should start with Upper Case")
    @Column(name = "author")
    private String author;

    @Min(value = 0, message = "Year should be greater than 0")
    @Column(name = "year")
    private int year;


    @Column(name = "book_assigned")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date bookAssigned;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    public Book() {
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getBookAssigned() {
        return bookAssigned;
    }

    public void setBookAssigned(Date bookAssigned) {
        this.bookAssigned = bookAssigned;
    }

    public void setOverdue() {
        Date currentDate = new Date();
        Date assignDate = bookAssigned;
        long diff = Math.abs(currentDate.getTime()-assignDate.getTime());
        long res = TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
        isOverdue = res>10;
    }

    public boolean isOverdue() {
        setOverdue();
        return isOverdue;
    }
}

package com.example.BookManagmentSystem.service;

import com.example.BookManagmentSystem.entity.Book;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {

    private static final List<Book> books = new ArrayList<>();

    // Seed data
    static {
        books.add(new Book(1, "Clean Code", "Robert C. Martin", 450.0));
        books.add(new Book(2, "Effective Java", "Joshua Bloch", 700.0));
        books.add(new Book(3, "Design Patterns", "Erich Gamma", 650.0));
    }

    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    public Optional<Book> findById(int id){
        for (Book b : books) {
            if (Objects.equals(b.getId(), id)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    public Book add(Book book){

        if(findById(book.getId()).isPresent()){
            throw new IllegalArgumentException("Book with id already exists: " + book.getId());
        }
        else {
            int nextId = books.stream().map(Book::getId).filter(Objects::nonNull).max(Integer::compareTo).orElse(0) + 1;
            book.setId(nextId);
        }
        books.add(book);
        return book;
    }

    public Optional<Book> update(Integer id, Book data) {
        return findById(id).map(existing -> {
            if (data.getTitle() != null) existing.setTitle(data.getTitle());
            if (data.getAuthor() != null) existing.setAuthor(data.getAuthor());
            if (data.getPrice() != 0) existing.setPrice(data.getPrice());
            return existing;
        });
    }

    public boolean delete(Integer id) {
        return books.removeIf(b -> Objects.equals(b.getId(), id));
    }

    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(b -> b.getAuthor() != null && b.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }

    public List<Book> findCheaperThan(double price) {
        return books.stream()
                .filter(book -> book.getPrice() < price)
                .collect(Collectors.toList());
    }

    public int count() {
        return books.size();
    }

    public Optional<Book> mostExpensive() {
        return books.stream()
                .max((b1, b2) -> Double.compare(b1.getPrice(), b2.getPrice()));
    }

}

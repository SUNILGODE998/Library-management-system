package com.example.BookManagmentSystem.controller;

import com.example.BookManagmentSystem.entity.Book;
import com.example.BookManagmentSystem.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    // POST /books - add
    @PostMapping
    public ResponseEntity<Book> add(@RequestBody Book book) {
        try {
            Book created = service.add(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    // GET /books - all
    @GetMapping
    public List<Book> getAll() {
        return service.findAll();
    }

    // GET /books/{id} - by id
    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable Integer id) {
        Optional<Book> b = service.findById(id);
        return b.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /books/{id} - update
    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable Integer id, @RequestBody Book book) {
        Optional<Book> updated = service.update(id, book);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /books/{id} - delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET /books/search?author=xyz - by author
    @GetMapping("/search")
    public List<Book> byAuthor(@RequestParam("author") String author) {
        return service.findByAuthor(author);
    }

    // GET /books/filter?price=500 - cheaper than price
    @GetMapping("/filter")
    public List<Book> cheaperThan(@RequestParam("price") double price) {
        return service.findCheaperThan(price);
    }

    // Bonus: GET /books/count - total number
    @GetMapping("/count")
    public int count() {
        return service.count();
    }

    // Bonus: GET /books/most-expensive - most expensive book
    @GetMapping("/most-expensive")
    public ResponseEntity<Book> mostExpensive() {
        return service.mostExpensive()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}

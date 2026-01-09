package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.service.BookService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // ================= ADMIN =================

    @PostMapping("/admin/books")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping("/admin/books/{id}")
    public Book updateBook(@PathVariable int id,
            @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/admin/books/{id}")
    public void deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
    }

    // ================= PUBLIC =================

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    // 🔍 SEARCH / FILTER / SORT
    @GetMapping("/books/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return bookService.searchBooks(
                keyword, categoryId, sortBy, direction);
    }
}

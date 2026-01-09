package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Book;
import com.bookstore.onlinebookstore.entity.Category;
import com.bookstore.onlinebookstore.repository.BookRepository;
import com.bookstore.onlinebookstore.repository.CategoryRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository,
            CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    // ================= ADMIN =================

    public Book createBook(Book book) {
        int categoryId = book.getCategory().getId();

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        book.setCategory(category);
        return bookRepository.save(book);
    }

    public Book updateBook(int id, Book updatedBook) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setPrice(updatedBook.getPrice());
        book.setStockQuantity(updatedBook.getStockQuantity());
        book.setDescription(updatedBook.getDescription());
        book.setImageUrl(updatedBook.getImageUrl());

        if (updatedBook.getCategory() != null) {
            int categoryId = updatedBook.getCategory().getId();
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            book.setCategory(category);
        }

        return bookRepository.save(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    // ================= PUBLIC =================

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(int id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // 🔍 SEARCH / FILTER / SORT
    public List<Book> searchBooks(
            String keyword,
            Integer categoryId,
            String sortBy,
            String direction) {
        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(direction)
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                sortBy != null ? sortBy : "title");

        if (keyword != null && categoryId != null) {
            return bookRepository
                    .findByCategory_IdAndTitleContainingIgnoreCase(
                            categoryId, keyword, sort);
        }

        if (keyword != null) {
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                            keyword, keyword, sort);
        }

        if (categoryId != null) {
            return bookRepository.findByCategory_Id(categoryId, sort);
        }

        return bookRepository.findAll(sort);
    }
}

package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    // 🔍 Search
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author, Sort sort);

    // 🗂 Filter by category
    List<Book> findByCategory_Id(int categoryId, Sort sort);

    // 🔍 + 🗂 Combined
    List<Book> findByCategory_IdAndTitleContainingIgnoreCase(
            int categoryId, String keyword, Sort sort);
}

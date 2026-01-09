package com.bookstore.onlinebookstore.repository;

import com.bookstore.onlinebookstore.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("author")), pattern));
        };
    }

    public static Specification<Book> hasCategory(int categoryId) {
        return (root, query, cb) -> {
            if (categoryId <= 0) {
                return null;
            }
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }
}

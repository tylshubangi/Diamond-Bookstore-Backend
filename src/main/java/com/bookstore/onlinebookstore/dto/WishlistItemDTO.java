package com.bookstore.onlinebookstore.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class WishlistItemDTO {

    private int itemId;
    private int bookId;
    private String title;
    private BigDecimal price;
    private String imageUrl;
}

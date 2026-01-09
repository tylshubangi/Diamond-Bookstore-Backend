package com.bookstore.onlinebookstore.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class WishlistResponseDTO {

    private int wishlistId;
    private List<WishlistItemDTO> items;
}

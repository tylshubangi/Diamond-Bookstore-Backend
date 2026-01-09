package com.bookstore.onlinebookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponseDTO {

    private int cartId;
    private List<CartItemResponseDTO> items;
    private BigDecimal grandTotal;

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<CartItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponseDTO> items) {
        this.items = items;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(BigDecimal grandTotal) {
        this.grandTotal = grandTotal;
    }

    // getters & setters
}

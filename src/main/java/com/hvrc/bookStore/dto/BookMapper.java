package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.model.Book;

public class BookMapper {
    public static BookDTO toBookDTO(Book book) {
        return new BookDTO(book.getId(), book.getName(), book.getAuthor(), book.getDescription(), book.getCategory(), book.getPrice(), book.getQuantity());
    }
}

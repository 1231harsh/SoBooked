package com.hvrc.bookStore.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookResponseDTO {
    private BookDTO book;
    private List<BookDTO> similarBooks;

    public BookResponseDTO(BookDTO book, List<BookDTO> similarBooks) {
        this.book = book;
        this.similarBooks = similarBooks;
    }
}

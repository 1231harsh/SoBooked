package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.entity.SavedBook;
import lombok.Data;

@Data
public class SavedBookDTO {

    private Long id;
    private Long bookId;
    private Long userId;
    private boolean liked;

    public SavedBookDTO(SavedBook savedBook) {
        this.id = savedBook.getId();
        this.bookId = savedBook.getBook().getId();
        this.liked = savedBook.isLiked();
        this.userId = savedBook.getUser().getId();
    }
}

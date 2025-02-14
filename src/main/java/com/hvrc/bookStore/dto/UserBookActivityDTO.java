package com.hvrc.bookStore.dto;

import lombok.Data;

@Data
public class UserBookActivityDTO {
    private Long bookId;
    private String actionType;

    public UserBookActivityDTO() {
    }

    public UserBookActivityDTO(Long bookId, String actionType) {
        this.bookId = bookId;
        this.actionType = actionType;
    }
}

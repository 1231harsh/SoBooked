package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.entity.UserBookActivity;
import lombok.Data;

@Data
public class UserBookActivityDTO {
    private Long id;
    private Long userId;
    private Long bookId;
    private String actionType;

    public UserBookActivityDTO() {
    }

    public UserBookActivityDTO(Long id, Long userId, Long bookId, String actionType) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.actionType = actionType;
    }

    public UserBookActivityDTO(UserBookActivity userBookActivity) {
        this.id = userBookActivity.getId();
        this.bookId = userBookActivity.getBook().getId();
        this.userId = userBookActivity.getUser().getId();
        this.actionType = userBookActivity.getActionType();
    }
}

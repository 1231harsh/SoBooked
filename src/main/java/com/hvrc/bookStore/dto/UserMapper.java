package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        return userDTO;
    }
}

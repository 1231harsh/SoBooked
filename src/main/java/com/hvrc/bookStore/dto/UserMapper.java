package com.hvrc.bookStore.dto;

import com.hvrc.bookStore.entity.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        return userDTO;
    }
}

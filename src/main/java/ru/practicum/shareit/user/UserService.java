package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {
    UserDto createUser(UserCreateDto userDto);

    UserDto updateUser(UserUpdateDto userDto, long userId);

    UserDto getUserById(long userId);

    void deleteUser(long id);
}

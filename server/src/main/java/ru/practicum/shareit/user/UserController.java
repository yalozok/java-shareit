package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    UserDto createUser(@RequestBody UserCreateDto userDto) {
        log.info("==> Create user: {}", userDto);
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@RequestBody UserUpdateDto userDto,
                       @PathVariable long userId) {
        log.info("==> Update user: {}", userDto);
        return userService.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable long userId) {
        log.info("==> Get user: {}", userId);
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable long userId) {
        log.info("==> Delete user: {}", userId);
        userService.deleteUser(userId);
    }
}

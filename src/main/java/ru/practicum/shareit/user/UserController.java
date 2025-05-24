package ru.practicum.shareit.user;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    UserDto createUser(@Validated @RequestBody UserCreateDto userDto) {
        log.info("==> Create user: {}", userDto);
        UserDto user = userService.createUser(userDto);
        log.info("<== User created: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    UserDto updateUser(@Validated @RequestBody UserUpdateDto userDto,
                       @PathVariable @PositiveOrZero Long userId) {
        log.info("==> Update user: {}", userDto);
        UserDto user = userService.updateUser(userDto, userId);
        log.info("<== User updated: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    UserDto getUserById(@PathVariable @PositiveOrZero Long userId) {
        log.info("==> Get user: {}", userId);
        UserDto user = userService.getUserById(userId);
        log.info("<== User: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    void deleteUser(@PathVariable @PositiveOrZero Long userId) {
        log.info("==> Delete user: {}", userId);
        userService.deleteUser(userId);
    }
}

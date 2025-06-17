package ru.practicum.shareit.user;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    ResponseEntity<Object> createUser(@Validated @RequestBody UserCreateDto userDto) {
        log.info("==> Create user: {}", userDto);
        ResponseEntity<Object> user = userClient.createUser(userDto);
        log.info("<== User created: {}", user);
        return user;
    }

    @PatchMapping("/{userId}")
    ResponseEntity<Object> updateUser(@Validated @RequestBody UserUpdateDto userDto,
                       @PathVariable @PositiveOrZero Long userId) {
        log.info("==> Update user: {}", userDto);
        ResponseEntity<Object> user = userClient.updateUser(userDto, userId);
        log.info("<== User updated: {}", user);
        return user;
    }

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUserById(@PathVariable @PositiveOrZero Long userId) {
        log.info("==> Get user: {}", userId);
        ResponseEntity<Object> user = userClient.getUserById(userId);
        log.info("<== User: {}", user);
        return user;
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable @PositiveOrZero Long userId) {
        log.info("==> Delete user: {}", userId);
        return userClient.deleteUser(userId);
    }
}

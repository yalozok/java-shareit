package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(long itemId);

    void deleteUser(long userId);

    boolean isEmailExist(String email);
}

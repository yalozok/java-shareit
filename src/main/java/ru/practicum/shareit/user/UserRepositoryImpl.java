package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class UserRepositoryImpl implements UserRepository {
    HashMap<Long, User> users = new HashMap<>();
    Set<String> uniqueEmails = new HashSet<>();
    private long id = 0;

    @Override
    public boolean isEmailExist(String email) {
        return uniqueEmails.contains(email);
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        uniqueEmails.add(user.getEmail());
        return user;
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        String oldEmail = users.get(id).getEmail();
        String newEmail = user.getEmail();
        if (!newEmail.equals(oldEmail)) {
            uniqueEmails.remove(oldEmail);
            uniqueEmails.add(newEmail);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(long itemId) {
        return Optional.ofNullable(users.get(itemId));
    }

    @Override
    public void deleteUser(long userId) {
        User user = users.remove(userId);
        uniqueEmails.remove(user.getEmail());
    }
}

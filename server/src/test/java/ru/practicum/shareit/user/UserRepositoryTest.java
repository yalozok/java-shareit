package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void existByEmail_EmailExisted_True() {
        boolean isExist = userRepository.existsByEmail(TestData.owner.getEmail());
        assertTrue(isExist);
    }

    @Test
    void existByEmail_EmailNotExisted_Exception() {
        boolean isExist = userRepository.existsByEmail("test@test.com");
        assertFalse(isExist);
    }
}


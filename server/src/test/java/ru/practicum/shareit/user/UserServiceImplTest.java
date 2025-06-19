package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.TestDto;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.exception.UserAlreadyExistException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class UserServiceImplTest {
    private final UserService userService;
    private final UserMapper userMapper;
    private final EntityManager em;

    @Test
    void createUserWithExistingEmail_ConflictException() {
        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> userService.createUser(TestDto.userToCreateWithExistingEmail));
        assertThat(exception.getMessage(),
                equalTo("User with email " + TestDto.userToCreateWithExistingEmail.getEmail() + " already exists"));
    }

    @Test
    void updateUser_UserIsUpdated() {
        UserDto user = userService.updateUser(TestDto.userToUpdate, TestData.owner.getId());
        assertThat(user.getEmail(), equalTo(TestDto.userToUpdate.getEmail()));

        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class);
        query.setParameter("userId", user.getId());
        User userFromDB = query.getSingleResult();
        UserDto expected = userMapper.toDto(userFromDB);
        assertThat(user.getEmail(), equalTo(expected.getEmail()));
    }

    @Test
    void updateUserWithExistingEmail_UserAlreadyExistException() {
        UserUpdateDto userUpdateDtoDto = new UserUpdateDto(null, "bob@example.com");
        UserAlreadyExistException exception = assertThrows(
                UserAlreadyExistException.class,
                () -> userService.updateUser(userUpdateDtoDto, TestData.owner.getId())
        );
        assertThat(exception.getMessage(), equalTo("User with email " + userUpdateDtoDto.getEmail() + " already exists"));
    }

    @Test
    void updateUserWithUnknownId_NotFoundUseException() {
        NotFoundUserException exception = assertThrows(
                NotFoundUserException.class,
                () -> userService.updateUser(TestDto.userToUpdate, Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("User not found: " + Long.MAX_VALUE));
    }

    @Test
    void getUserById_UserIsFound() {
        UserDto user = userService.getUserById(TestData.owner.getId());
        assertThat(user.getId(), equalTo(TestData.owner.getId()));
    }

    @Test
    void getUserById_UserIsNotFound() {
        NotFoundUserException exception = assertThrows(
                NotFoundUserException.class,
                () -> userService.getUserById(Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(),
                equalTo("User not found: " + Long.MAX_VALUE));
    }

    @Test
    void deleteUser_UserIsDeleted() {
        userService.deleteUser(TestData.owner.getId());
        TypedQuery<User> queryUser = em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class);
        queryUser.setParameter("userId", TestData.owner.getId());
        List<User> userFromDB = queryUser.getResultList();
        assertThat(userFromDB, is(empty()));

        TypedQuery<Item> queryItem = em.createQuery("SELECT i FROM Item i WHERE i.owner.id = :userId", Item.class);
        queryItem.setParameter("userId", TestData.owner.getId());
        List<Item> itemsFromDB = queryItem.getResultList();
        assertThat(itemsFromDB, is(empty()));
    }

    @Test
    void toModel_shouldMapFieldsCorrectly() {
        User user = userMapper.toModel(TestDto.userToCreate);
        assertThat(user.getEmail(), equalTo(TestDto.userToCreate.getEmail()));
        assertThat(user.getName(), equalTo(TestDto.userToCreate.getName()));
    }
}

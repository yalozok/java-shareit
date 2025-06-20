package ru.practicum.shareit.user;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserTest {

    @Test
    public void testGetterSetter() {
        User user = new User();
        user.setId(1L);
        user.setName("name");
        user.setEmail("email@email.com");

        assertThat(user.getId(), equalTo(1L));
        assertThat(user.getName(), equalTo("name"));
        assertThat(user.getEmail(), equalTo("email@email.com"));
    }

}

package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.TestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserMapper userMapper;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto(1, "Alice Smith", "alice@example.com");
        when(userService.createUser(TestDto.userToCreate)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(TestDto.userToCreate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestDto.userToCreate.getName())))
                .andExpect(jsonPath("$.email", is(TestDto.userToCreate.getEmail())));
        verify(userService, times(1)).createUser(TestDto.userToCreate);
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = new UserDto(1, "Alice Smith", "alice.smith@example.com");
        when(userService.updateUser(TestDto.userToUpdate, TestData.owner.getId())).thenReturn(userDto);

        mockMvc.perform(patch("/users/" + TestData.owner.getId())
                        .content(mapper.writeValueAsString(TestDto.userToUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestData.owner.getName())))
                .andExpect(jsonPath("$.email", is(TestDto.userToUpdate.getEmail())));
        verify(userService, times(1)).updateUser(TestDto.userToUpdate, TestData.owner.getId());
    }

    @Test
    void getUserById() throws Exception {
        UserDto userDto = userMapper.toDto(TestData.owner);
        when(userService.getUserById(TestData.owner.getId())).thenReturn(userDto);
        mockMvc.perform(get("/users/" + TestData.owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestData.owner.getName())))
                .andExpect(jsonPath("$.email", is(TestData.owner.getEmail())));
        verify(userService, times(1)).getUserById(TestData.owner.getId());
    }

    @Test
    void deleteUserById() throws Exception {
        doNothing().when(userService).deleteUser(TestData.owner.getId());
        mockMvc.perform(delete("/users/" + TestData.owner.getId()))
                .andExpect(status().isOk());
    }
}

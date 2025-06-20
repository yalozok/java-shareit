package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ItemMapper itemMapper;

    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    void createItem() throws Exception {
        ItemDto item = itemMapper.toDto(TestData.item1, TestData.owner);
        when(itemService.createItem(TestDto.itemCreateDto, TestData.owner.getId())).thenReturn(item);
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(TestDto.itemCreateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TestData.owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestData.item1.getName())))
                .andExpect(jsonPath("$.description", is(TestData.item1.getDescription())));
        verify(itemService, times(1)).createItem(TestDto.itemCreateDto, TestData.owner.getId());
    }

    @Test
    void updateItem() throws Exception {
        ItemDto item = new ItemDto(1, "Drill electric", "Electric drill with variable speed", true,
                new ItemDto.UserDto(TestData.owner.getId(), TestData.owner.getName()), null, null, null, null);
        when(itemService.updateItem(TestData.owner.getId(), TestDto.itemUpdateDto, TestData.item1.getId())).thenReturn(item);

        mockMvc.perform(patch("/items/" + TestData.item1.getId())
                        .content(mapper.writeValueAsString(TestDto.itemUpdateDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", TestData.owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestDto.itemUpdateDto.getName())))
                .andExpect(jsonPath("$.description", is(TestData.item1.getDescription())));
        verify(itemService, times(1)).updateItem(TestData.owner.getId(), TestDto.itemUpdateDto, TestData.item1.getId());
    }

    @Test
    void getItemById() throws Exception {
        ItemDto item = itemMapper.toDto(TestData.item1, TestData.owner);
        when(itemService.getItemById(TestData.item1.getId())).thenReturn(item);
        mockMvc.perform(get("/items/" + TestData.item1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(TestData.item1.getName())))
                .andExpect(jsonPath("$.description", is(TestData.item1.getDescription())));
        verify(itemService, times(1)).getItemById(TestData.item1.getId());
    }

    @Test
    void getItemsByUser() throws Exception {
        List<ItemDto> items = List.of(
                itemMapper.toDto(TestData.item1, TestData.owner),
                itemMapper.toDto(TestData.item2, TestData.owner),
                itemMapper.toDto(TestData.item3, TestData.owner)
        );
        when(itemService.getItemsByUser(TestData.owner.getId())).thenReturn(items);
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", TestData.owner.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(items.size())));
        verify(itemService, times(1)).getItemsByUser(TestData.owner.getId());
    }

    @Test
    void searchItem() throws Exception {
        List<ItemDto> item = List.of(itemMapper.toDto(TestData.item1, TestData.owner));
        String text = "ril";
        when(itemService.searchItem(text)).thenReturn(item);

        mockMvc.perform(get("/items/search")
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(item.size())))
                .andExpect(jsonPath("$[0].id", is(1)));
        verify(itemService, times(1)).searchItem(text);

    }

    @Test
    void deleteItem() throws Exception {
        doNothing().when(itemService).deleteItem(TestData.item1.getId());
        mockMvc.perform(delete("/items/" + TestData.item1.getId()))
                .andExpect(status().isOk());
    }
}

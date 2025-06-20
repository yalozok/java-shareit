package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestTestControllerTest {
    @MockBean
    private ItemRequestService requestService;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ItemRequestMapper requestMapper;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createRequest() throws Exception {
        ItemRequestDto requestDto = requestMapper.toDto(TestData.request1);
        ItemRequestCreateDto requestCreateDto = new ItemRequestCreateDto("Request for a drill");
        when(requestService.createRequest(requestCreateDto, TestData.requestor1.getId())).thenReturn(requestDto);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", TestData.requestor1.getId())
                        .content(objectMapper.writeValueAsString(requestCreateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
        verify(requestService, times(1)).createRequest(requestCreateDto, TestData.requestor1.getId());
    }

    @Test
    void getRequestByRequestor() throws Exception {
        List<ItemRequestDto> requests = List.of(
                requestMapper.toDto(TestData.request1),
                requestMapper.toDto(TestData.request3)
        );
        when(requestService.getRequestsByRequestor(TestData.requestor1.getId())).thenReturn(requests);
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", TestData.requestor1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(requests.size())));
        verify(requestService, times(1)).getRequestsByRequestor(TestData.requestor1.getId());
    }

    @Test
    void getAllRequests() throws Exception {
        List<ItemRequestDto> requests = List.of(
                requestMapper.toDto(TestData.request1),
                requestMapper.toDto(TestData.request2),
                requestMapper.toDto(TestData.request3)
        );
        when(requestService.getAllRequests()).thenReturn(requests);
        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(requests.size())));
        verify(requestService, times(1)).getAllRequests();
    }

    @Test
    void getRequestById() throws Exception {
        ItemRequestDto requestDto = requestMapper.toDto(TestData.request1);
        when(requestService.getRequestById(TestData.request1.getId())).thenReturn(requestDto);
        mockMvc.perform(get("/requests/" + TestData.request1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
        verify(requestService, times(1)).getRequestById(TestData.request1.getId());
    }
}

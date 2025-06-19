package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.TestDto;
import ru.practicum.shareit.exception.NotFoundRequestException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemRequestServiceTest {
    private final ItemRequestService requestService;
    private final ItemRequestMapper requestMapper;

    @Test
    void createRequestByNotExistingUser_NotFoundUserException() {
        NotFoundUserException exception = assertThrows(
                NotFoundUserException.class,
                () -> requestService.createRequest(TestDto.requestCreateDto, Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("User not found: " + Long.MAX_VALUE));
    }

    @Test
    void getRequestByRequestor_ListRequests() {
        List<ItemRequestDto> requests = requestService.getRequestsByRequestor(TestData.requestor1.getId());
        assertThat(requests.size(), equalTo(2));
    }

    @Test
    void getRequestByUserWithNoRequest_EmptyList() {
        List<ItemRequestDto> requests = requestService.getRequestsByRequestor(TestData.owner.getId());
        assertThat(requests, is(empty()));
    }

    @Test
    void getRequestByNotExistingRequestor_NotFoundUserException() {
        NotFoundUserException exception = assertThrows(
                NotFoundUserException.class,
                () -> requestService.getRequestsByRequestor(Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("User not found: " + Long.MAX_VALUE));
    }

    @Test
    void getAllRequests_ListRequests() {
        List<ItemRequestDto> requests = requestService.getAllRequests();
        assertThat(requests.size(), equalTo(3));
    }

    @Test
    void getRequestById_Request() {
        ItemRequestDto request = requestService.getRequestById(TestData.request1.getId());
        assertThat(request.getDescription(), equalTo(TestData.request1.getDescription()));
    }

    @Test
    void getRequestByNotExistingId_NotFoundRequestException() {
        NotFoundRequestException exception = assertThrows(
                NotFoundRequestException.class,
                () -> requestService.getRequestById(Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("Request with id " + Long.MAX_VALUE + " not found"));
    }

    @Test
    void toModel_ShouldMapFieldCorrectly() {
        ItemRequest request = requestMapper.toModel(TestDto.requestCreateDto, TestData.requestor1);
        assertThat(request.getDescription(), equalTo(TestDto.requestCreateDto.getDescription()));
        assertThat(request.getRequestor(), equalTo(TestData.requestor1));
    }
}

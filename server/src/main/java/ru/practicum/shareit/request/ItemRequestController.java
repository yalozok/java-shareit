package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @PostMapping
    ItemRequestDto createRequest(@RequestHeader(SHARER_USER_ID) long requestorId,
                                 @RequestBody ItemRequestCreateDto requestCreateDto) {
        log.info("==> Create request: {}", requestCreateDto);
        return requestService.createRequest(requestCreateDto, requestorId);
    }

    @GetMapping
    List<ItemRequestDto> getRequestsByRequestor(@RequestHeader(SHARER_USER_ID) long requestorId) {
        log.info("==> Get requests by requestor: {}", requestorId);
        return requestService.getRequestsByRequestor(requestorId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllRequests() {
        log.info("==> Get all requests");
        return requestService.getAllRequests();
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@PathVariable("requestId") long requestId) {
        log.info("==> Get request by id: {}", requestId);
        return requestService.getRequestById(requestId);
    }
}

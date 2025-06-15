package ru.practicum.shareit.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService requestService;

    @PostMapping
    ItemRequestDto createRequest(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long requestorId,
            @Validated @RequestBody ItemRequestCreateDto requestCreateDto) {
        log.info("==> Create request: {}", requestCreateDto);
        ItemRequestDto requestDto = requestService.createRequest(
                requestCreateDto, requestorId
        );
        log.info("<== Created request: {}", requestDto);
        return requestDto;
    }

    @GetMapping
    List<ItemRequestDto> getRequestsByRequestor(@RequestHeader(SHARER_USER_ID) @PositiveOrZero long requestorId) {
        log.info("==> Get requests by requestor: {}", requestorId);
        List<ItemRequestDto> requests = requestService.getRequestsByRequestor(requestorId);
        log.info("<== Get all requests by requestor: {}", requests);
        return requests;
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAllRequests() {
        log.info("==> Get all requests");
        List<ItemRequestDto> requests = requestService.getAllRequests();
        log.info("<== Get all requests");
        return requests;
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getRequestById(@PathVariable("requestId") @PositiveOrZero long requestId) {
        log.info("==> Get request by id: {}", requestId);
        ItemRequestDto requestDto = requestService.getRequestById(requestId);
        log.info("<== Get request by id: {}", requestDto);
        return requestDto;
    }
}

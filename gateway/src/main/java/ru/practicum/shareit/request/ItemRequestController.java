package ru.practicum.shareit.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestClient requestClient;

    @PostMapping
    ResponseEntity<Object> createRequest(@RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long requestorId,
                                         @Validated @RequestBody ItemRequestCreateDto requestCreateDto) {
        log.info("==> Create request: {}", requestCreateDto);
        ResponseEntity<Object> requestDto = requestClient.createRequest(
                requestorId, requestCreateDto
        );
        log.info("<== Created request: {}", requestDto);
        return requestDto;
    }

    @GetMapping
    ResponseEntity<Object> getRequestsByRequestor(@RequestHeader(SHARER_USER_ID) @NotNull @PositiveOrZero Long requestorId) {
        log.info("==> Get requests by requestor: {}", requestorId);
        ResponseEntity<Object> requests = requestClient.getRequestsByRequestor(requestorId);
        log.info("<== Get all requests by requestor: {}", requests);
        return requests;
    }

    @GetMapping("/all")
    ResponseEntity<Object> getAllRequests() {
        log.info("==> Get all requests");
        ResponseEntity<Object> requests = requestClient.getAllRequests();
        log.info("<== Get all requests");
        return requests;
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequestById(@PathVariable("requestId") @NotNull @PositiveOrZero Long requestId) {
        log.info("==> Get request by id: {}", requestId);
        ResponseEntity<Object> requestDto = requestClient.getRequestById(requestId);
        log.info("<== Get request by id: {}", requestDto);
        return requestDto;
    }
}

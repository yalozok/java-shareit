package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundRequestException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemRequestService {
    private final ItemRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper requestMapper;
    private final ItemRepository itemRepository;

    public ItemRequestDto createRequest(ItemRequestCreateDto requestCreateDto,
                                        long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundUserException(requestorId));
        ItemRequest itemRequest = requestRepository.save(requestMapper.toModel(requestCreateDto, requestor));
        return requestMapper.toDto(itemRequest);
    }

    public List<ItemRequestDto> getRequestsByRequestor(long requestorId) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundUserException(requestorId));
        List<ItemRequest> requests = requestRepository.findByRequestorOrderByCreatedDesc(requestor);
        return requests.stream()
                .map(request -> {
                    List<Item> items = itemRepository.findAllByRequest(request);
                    return requestMapper.toDtoWithItems(request, items);
                })
                .toList();
    }

    public List<ItemRequestDto> getAllRequests() {
        List<ItemRequest> requests = requestRepository.findAllByOrderByCreatedDesc();
        return requests.stream()
                .map(requestMapper::toDto)
                .toList();
    }

    public ItemRequestDto getRequestById(long requestId) {
        ItemRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundRequestException(requestId));
        List<Item> items = Optional.ofNullable(itemRepository.findAllByRequest(request))
                .orElseGet(ArrayList::new);
        return requestMapper.toDtoWithItems(request, items);
    }

}

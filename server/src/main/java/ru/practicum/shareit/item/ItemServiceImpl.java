package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundRequestException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository requestRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto createItem(ItemCreateDto itemDto, long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));
        Long requestId = itemDto.getRequestId();
        Item item;
        if (requestId != null) {
            ItemRequest request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundRequestException(requestId));
            item = itemMapper.toModelWithRequest(itemDto, owner, request);
        } else {
            item = itemMapper.toModel(itemDto, owner);
        }
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem, owner);
    }

    @Override
    public ItemDto updateItem(long ownerId, ItemUpdateDto itemDto, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));

        if (!itemDto.getOwner().equals(ownerId)) {
            throw new ForbiddenOperationException(itemDto.getOwner());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));

        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem, owner);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));

        long ownerId = item.getOwner().getId();
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));
        List<CommentDto> comments = getCommentsByItemId(itemId);
        return itemMapper.toDtoWithComments(item, owner, comments);
    }

    @Override
    public List<ItemDto> getItemsByUser(long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundUserException(ownerId));

        List<Item> items = itemRepository.findByOwner(owner);
        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = getCommentsByItemId(item.getId());
                    ItemDto itemDto = itemMapper.toDtoWithComments(item, owner, comments);

                    LocalDateTime now = LocalDateTime.now();
                    Optional<Booking> lastBookingOpt = bookingRepository.findLastBooking(item.getId(), now);
                    lastBookingOpt.ifPresent(
                            lastBooking -> itemDto.setLastBooking(
                                    new ItemDto.BookingDto(lastBooking.getId(),
                                            lastBooking.getStart(), lastBooking.getEnd())));

                    Optional<Booking> nextBookingOpt = bookingRepository.findNextBooking(item.getId(), now);
                    nextBookingOpt.ifPresent(
                            nextBooking -> itemDto.setNextBooking(
                                    new ItemDto.BookingDto(nextBooking.getId(),
                                            nextBooking.getStart(), nextBooking.getEnd()))
                    );
                    return itemDto;
                })
                .toList();
    }


    @Override
    public List<ItemDto> searchItem(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchItem(text);
        return items.stream()
                .map(item -> {
                    long ownerId = item.getOwner().getId();
                    User owner = userRepository.findById(ownerId)
                            .orElseThrow(() -> new NotFoundUserException(ownerId));
                    return itemMapper.toDto(item, owner);
                })
                .toList();
    }

    @Override
    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));
        bookingRepository.deleteByItem(item);
        commentRepository.deleteByItem(item);
        itemRepository.deleteById(itemId);
    }

    @Override
    public CommentDto addComment(CommentCreateDto commentCreateDto, long itemId, long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundUserException(authorId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundItemException(itemId));

        Booking booking = bookingRepository.findByItem_IdAndBooker_Id(itemId, authorId)
                .orElseThrow(() -> new ForbiddenOperationException(authorId));

        if (!booking.getEnd().isBefore(LocalDateTime.now())
                || !booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("You can leave a comment once your booking has ended");
        }
        Comment comment = commentRepository.save(commentMapper.toModel(commentCreateDto, item, author));
        return commentMapper.toDto(comment, author);
    }

    private List<CommentDto> getCommentsByItemId(long itemId) {
        return commentRepository.findByItem_Id(itemId)
                .stream()
                .map(comment -> commentMapper.toDto(comment, comment.getAuthor()))
                .toList();
    }

}

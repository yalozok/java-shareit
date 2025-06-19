package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.TestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ForbiddenOperationException;
import ru.practicum.shareit.exception.NotFoundItemException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class ItemServiceImplTest {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final EntityManager em;

    @Test
    void createItemByNotExistedUser_NotFoundUserException() {
        NotFoundUserException exception = assertThrows(
                NotFoundUserException.class,
                () -> itemService.createItem(TestDto.itemCreateDto, Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("User not found: " + Long.MAX_VALUE));
    }

    @Test
    void updateItemName_ItemUpdated() {
        ItemDto item = itemService.updateItem(TestDto.itemUpdateDto, TestData.item1.getId());
        assertThat(item.getName(), equalTo(TestDto.itemUpdateDto.getName()));

        TypedQuery<Item> query = em.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class);
        query.setParameter("itemId", item.getId());
        Item itemFromDB = query.getSingleResult();
        ItemDto expected = itemMapper.toDto(itemFromDB, TestData.owner);
        assertThat(item.getName(), equalTo(expected.getName()));
    }

    @Test
    void updateNotExistingItem_NotFoundItemException() {
        NotFoundItemException exception = assertThrows(
                NotFoundItemException.class,
                () -> itemService.updateItem(TestDto.itemUpdateDto, Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("Item not found: " + Long.MAX_VALUE));
    }

    @Test
    void updateItemByNotOwner_ForbiddenOperationException() {
        ForbiddenOperationException exception = assertThrows(
                ForbiddenOperationException.class,
                () -> itemService.updateItem(TestDto.itemUpdateDtoByNotOwner, TestData.item1.getId())
        );
        assertThat(exception.getMessage(), equalTo("Forbidden operation for this user: " +
                TestDto.itemUpdateDtoByNotOwner.getOwner()));
    }

    @Test
    void getItemById_Item() {
        ItemDto item = itemService.getItemById(TestData.item1.getId());
        assertThat(item.getId(), equalTo(TestData.item1.getId()));
    }

    @Test
    void getItemByNotExistingId_NotFoundItemException() {
        NotFoundItemException exception = assertThrows(
                NotFoundItemException.class,
                () -> itemService.getItemById(Long.MAX_VALUE)
        );
        assertThat(exception.getMessage(), equalTo("Item not found: " + Long.MAX_VALUE));
    }

    @Test
    void getItemsByUser_ListItems() {
        List<ItemDto> items = itemService.getItemsByUser(TestData.owner.getId());
        assertThat(items.size(), equalTo(3));
    }

    @Test
    void searchItemByCorrectQuery_ListItems() {
        List<ItemDto> items = itemService.searchItem("ril");
        assertThat(items.size(), equalTo(1));
        assertThat(items.getFirst().getName(), equalTo(TestData.item1.getName()));
    }

    @Test
    void searchItemByEmptyQuery_ListItems() {
        List<ItemDto> items = itemService.searchItem("");
        assertThat(items, is(empty()));
    }

    @Test
    void searchItemByNotExistingQuery_EmptyList() {
        List<ItemDto> items = itemService.searchItem("pac");
        assertThat(items, is(empty()));
    }

    @Test
    void deleteItem_ItemDeleted() {
        itemService.deleteItem(TestData.item1.getId());
        TypedQuery<Item> queryItem = em.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class);
        queryItem.setParameter("itemId", TestData.item1.getId());
        List<Item> itemFromDB = queryItem.getResultList();
        assertThat(itemFromDB, is(empty()));

        TypedQuery<Booking> queryBooking = em.createQuery("SELECT b FROM Booking b WHERE b.item.id = :itemId", Booking.class);
        queryBooking.setParameter("itemId", TestData.item1.getId());
        List<Booking> bookingFromDB = queryBooking.getResultList();
        assertThat(bookingFromDB, is(empty()));

        TypedQuery<Comment> queryComment = em.createQuery("SELECT c FROM Comment c WHERE c.item.id = :itemId", Comment.class);
        queryComment.setParameter("itemId", TestData.item1.getId());
        List<Comment> commentFromDB = queryComment.getResultList();
        assertThat(commentFromDB, is(empty()));
    }

    @Test
    void addCommentWithBookingStatusRejected_ValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> itemService.addComment(TestDto.commentCreateDto,
                        TestData.item3.getId(), TestData.requestor1.getId())
        );
        assertThat(exception.getMessage(), equalTo("You can leave a comment once your booking has ended"));
    }

    @Test
    void addCommentWithBookingStatusWaiting_ValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> itemService.addComment(TestDto.commentCreateDto,
                        TestData.item2.getId(), TestData.requestor2.getId())
        );
        assertThat(exception.getMessage(), equalTo("You can leave a comment once your booking has ended"));
    }

    @Test
    void toItemModel_ShouldMapFieldsCorrectly() {
        Item item = itemMapper.toModel(TestDto.itemCreateDto, TestData.owner);
        assertThat(item.getName(), equalTo(TestDto.itemCreateDto.getName()));
        assertThat(item.getOwner(), equalTo(TestData.owner));
        assertThat(item.getDescription(), equalTo(TestDto.itemCreateDto.getDescription()));
        assertThat(item.isAvailable(), equalTo(TestDto.itemCreateDto.getAvailable()));
    }

    @Test
    void toCommentModel_ShouldMapFieldsCorrectly() {
        Comment comment = commentMapper.toModel(TestDto.commentCreateDto, TestData.item1, TestData.requestor1);
        assertThat(comment.getText(), equalTo(TestDto.commentCreateDto.getText()));
        assertThat(comment.getAuthor(), equalTo(TestData.requestor1));
        assertThat(comment.getItem(), equalTo(TestData.item1));
    }
}

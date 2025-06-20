package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestData;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemRequestTest {
    @Test
    public void testGetSetItemRequest() {
        LocalDateTime created = LocalDateTime.now().plusDays(1);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        itemRequest.setDescription("description");
        itemRequest.setRequestor(TestData.requestor1);
        itemRequest.setCreated(created);

        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo("description"));
        assertThat(itemRequest.getRequestor(), equalTo(TestData.requestor1));
        assertThat(itemRequest.getCreated(), equalTo(created));
    }
}

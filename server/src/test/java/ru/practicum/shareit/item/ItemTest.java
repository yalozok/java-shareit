package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemTest {
    @Test
    public void testGetSetName() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(TestData.owner);
        item.setRequest(TestData.request1);

        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo("name"));
        assertThat(item.getDescription(), equalTo("description"));
        assertThat(item.isAvailable(), equalTo(true));
        assertThat(item.getOwner(), equalTo(TestData.owner));
        assertThat(item.getRequest(), equalTo(TestData.request1));
    }
}

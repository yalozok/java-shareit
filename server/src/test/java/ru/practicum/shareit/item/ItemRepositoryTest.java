package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByOwner_shouldReturnListItem() {
        List<Item> itemsByOwner = itemRepository.findByOwner(TestData.owner);
        assertThat(itemsByOwner, hasSize(3));
    }

    @Test
    void searchItem_shouldReturnListItem() {
        List<Item> items = itemRepository.searchItem("ril");
        assertThat(items, hasSize(1));
        assertThat(items.getFirst().getName(), equalTo(TestData.item1.getName()));
    }

    @Test
    void findAllByRequest_shouldReturnListItem() {
        List<Item> items = itemRepository.findAllByRequest(TestData.request1);
        assertThat(items, hasSize(1));
        assertThat(items.getFirst().getName(), equalTo(TestData.item1.getName()));
    }
}

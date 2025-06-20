package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;


@DataJpaTest
public class ItemRequestRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findByRequestor_Requests() {
        List<ItemRequest> requests = itemRequestRepository.findByRequestorOrderByCreatedDesc(TestData.requestor1);
        assertThat(requests, hasSize(TestData.requestsByRequestor1.size()));
        assertThat(requests.getFirst().getId(), equalTo(TestData.request3.getId()));
    }

    @Test
    void findAll_Requests() {
        List<ItemRequest> requests = itemRequestRepository.findAllByOrderByCreatedDesc();
        assertThat(requests, hasSize(3));
    }
}

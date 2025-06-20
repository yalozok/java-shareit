package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByItemId_ListComment() {
        List<Comment> comments = commentRepository.findByItem_Id(TestData.item1.getId());
        assertThat(comments, hasSize(1));
        assertThat(comments.getFirst().getItem().getId(), equalTo(TestData.item1.getId()));
    }

    @Test
    void deleteByItem_CommentDeleted() {
        List<Comment> commentsBeforeDeleting = commentRepository.findByItem_Id(TestData.item1.getId());
        assertThat(commentsBeforeDeleting, hasSize(1));

        commentRepository.deleteByItem(TestData.item1);

        List<Comment> commentsAfterDeleting = commentRepository.findByItem_Id(TestData.item1.getId());
        assertThat(commentsAfterDeleting, hasSize(0));
    }
}

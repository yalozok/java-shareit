package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommentTest {
    @Test
    public void testGetSetComment() {
        LocalDateTime created = LocalDateTime.now().plusDays(1);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("comment description");
        comment.setItem(TestData.item1);
        comment.setAuthor(TestData.requestor1);
        comment.setCreated(created);

        assertThat(comment.getId(), equalTo(1L));
        assertThat(comment.getText(), equalTo("comment description"));
        assertThat(comment.getCreated(), equalTo(created));
        assertThat(comment.getAuthor(), equalTo(TestData.requestor1));
        assertThat(comment.getItem(), equalTo(TestData.item1));

    }
}

package com.conleos.views.form;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.conleos.data.entity.Form;
import com.conleos.core.Session;
import com.conleos.data.entity.Comment;
import com.conleos.data.repository.CommentRepository;
import com.conleos.data.service.CommentService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.messages.MessageInput;
import com.vaadin.flow.component.messages.MessageList;
import com.vaadin.flow.component.messages.MessageListItem;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

public class CommentView extends Div {

    public CommentView(Form form) {
        MessageList list = new MessageList();
        MessageInput input = new MessageInput();
        input.addSubmitListener(submitEvent -> {
            MessageListItem newMessage = new MessageListItem(
                    submitEvent.getValue(), Instant.now(), Session.getSessionFromVaadinSession(VaadinSession.getCurrent()).getUser().getFullName());
            newMessage.setUserColorIndex(2);
            List<MessageListItem> items = new ArrayList<>(list.getItems());
            items.add(newMessage);
            Comment comment = new Comment();
            comment.setForm(form);
            comment.setTime(newMessage.getTime().toString());
            comment.setUserId(Session.getSessionFromVaadinSession(VaadinSession.getCurrent()).getUser());
            comment.setComment(newMessage.getText());
            CommentService.getInstance().saveComment(comment);
            list.setItems(items);
        });

        List<Comment> comments = CommentService.getInstance().getAllCommentsByForm(form.getId());

        if (!comments.isEmpty()) {
            List<MessageListItem> oldItems = new ArrayList<>();
            for (Comment c : comments) {
                MessageListItem message = new MessageListItem(c.getComment(), Instant.parse(c.getTime()), c.getUserId().getFullName());
                message.setUserColorIndex((int) (Math.random() * 50));
                oldItems.add(message);
            }
            list.setItems(oldItems);
        }

            VerticalLayout chatLayout = new VerticalLayout(list, input);
            chatLayout.setHeight("500px");
            chatLayout.setWidth("400px");
            chatLayout.expand(list);
            add(chatLayout);


    }
}

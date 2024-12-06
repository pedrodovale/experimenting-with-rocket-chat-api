package com.pedrodovale.experimentingwith.rocketchat;

import static java.util.UUID.randomUUID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.pedrodovale.experimentingwith.rocketchat.api.model.ChatDiscussion;
import com.pedrodovale.experimentingwith.rocketchat.api.model.ChatUser;
import com.pedrodovale.experimentingwith.rocketchat.api.model.ChatUsersResponse;
import com.pedrodovale.experimentingwith.rocketchat.api.model.CreateDiscussionRequest;
import com.pedrodovale.experimentingwith.rocketchat.api.model.CreateDiscussionResponse;
import com.pedrodovale.experimentingwith.rocketchat.model.Book;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BooksController {

  private final RestClient restClient;
  private final MessageSource messageSource;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public Book createBook(@RequestBody @Valid Book book, Locale locale) {

    log.info("Creating book: {}", book);

    // save book to db and some other stuff ...

    createChat(book, locale);

    book.setId(randomUUID().toString());

    log.info("Book created: {}", book);

    return book;
  }

  private void createChat(Book book, Locale locale) {

    ChatUsersResponse chatUsersResponse =
        restClient.get().uri("/api/v1/users.list").retrieve().body(ChatUsersResponse.class);
    List<ChatUser> allUsers = chatUsersResponse.getUsers();
    log.debug("users: {}", allUsers);

    CreateDiscussionResponse createDiscussionResponse =
        restClient
            .post()
            .uri("/api/v1/rooms.createDiscussion")
            .body(
                new CreateDiscussionRequest("GENERAL", book.getTitle())
                    .reply(
                        messageSource.getMessage(
                            "chat.welcome.message",
                            new Object[] {
                              book.getTitle(), book.getAuthor(), book.getPublicationDate()
                            },
                            locale))
                    .users(allUsers.stream().map(ChatUser::getUsername).toList()))
            .retrieve()
            .body(CreateDiscussionResponse.class);
    ChatDiscussion discussion = createDiscussionResponse.getDiscussion();
    log.debug("discussion: {}", discussion);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  public void handMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("MethodArgumentNotValidException: {}", e.getMessage());
  }
}

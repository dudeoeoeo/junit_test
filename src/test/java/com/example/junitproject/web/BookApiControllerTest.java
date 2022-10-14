package com.example.junitproject.web;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.service.BookService;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 통합테스트 (C, S, R)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate template;

    private static ObjectMapper om;
    private static HttpHeaders headers;

    @BeforeAll
    public static void init() {
        om = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    public void saveDefaultBook() {
        final Book book = Book.builder()
                .id(1L)
                .title("junit")
                .author("spring")
                .build();
        bookRepository.save(book);
    }

    @Test
    public void saveBook_test() throws Exception {
        // given
        BookSaveReqDto reqDto = new BookSaveReqDto();
        reqDto.setTitle("통합테스트");
        reqDto.setAuthor("스프링부트");

        String body = om.writeValueAsString(reqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = template.exchange("/api/v1/book", HttpMethod.POST, request, String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo(reqDto.getTitle());
        assertThat(author).isEqualTo(reqDto.getAuthor());
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookList() throws Exception {
        // when
        final ResponseEntity<String> response = template.getForEntity("/api/v1/book", String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        int code = dc.read("$.code");
        List<BookRespDto> items = dc.read("$.body.items");

        assertThat(code).isEqualTo(1);
        assertThat(items.size()).isEqualTo(1);
        assertThat(items.isEmpty()).isFalse();
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void getBookOne() throws Exception {
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = template.exchange(
                "/api/v1/book/" + id,
                HttpMethod.GET,
                request,
                String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        int code = dc.read("$.code");
        int bookId = dc.read("$.body.id");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(code).isEqualTo(1);
        assertThat(bookId).isEqualTo(1);
        assertThat(title).isEqualTo("junit");
        assertThat(author).isEqualTo("spring");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void updateBook() throws Exception {
        // given
        Integer id = 1;
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("batch");
        bookSaveReqDto.setAuthor("baeldung");

        String body = om.writeValueAsString(bookSaveReqDto);

        // when
        HttpEntity<String> request = new HttpEntity<>(body, headers);
        final ResponseEntity<String> response = template.exchange(
                "/api/v1/book/" + id,
                HttpMethod.PUT,
                request,
                String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        int code = dc.read("$.code");
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("batch");
        assertThat(author).isEqualTo("baeldung");
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void deleteBook() throws Exception {
        // given
        Integer id = 1;

        // when
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        final ResponseEntity<String> response = template.exchange(
                "/api/v1/book/" + id,
                HttpMethod.DELETE,
                request,
                String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        int code = dc.read("$.code");
        final int statusCodeValue = response.getStatusCodeValue();

        assertThat(code).isEqualTo(1);
        assertThat(statusCodeValue).isEqualTo(200);
    }
}

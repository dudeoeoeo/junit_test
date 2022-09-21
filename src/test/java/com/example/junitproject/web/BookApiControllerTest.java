package com.example.junitproject.web;

import com.example.junitproject.service.BookService;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// 통합테스트 (C, S, R)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {

    @Autowired
    private BookService bookService;

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

    @Test
    public void getBookList() throws Exception {
        // when
        final ResponseEntity<String> response = template.getForEntity("/api/v1/book", String.class);

        // then
        DocumentContext dc = JsonPath.parse(response.getBody());
        List<BookRespDto> items = dc.read("$.body.items");

        assertThat(items.size()).isEqualTo(0);
        assertThat(items.isEmpty()).isTrue();
    }

}

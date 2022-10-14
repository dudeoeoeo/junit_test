package com.example.junitproject.service;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Test
    public void 책등록하기_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("junit");
        dto.setAuthor("spring");

        // stub
        when(bookRepository.save(any())).thenReturn(dto.toEntity());

        // when
        BookRespDto bookRespDto = bookService.createBook(dto);

        // then
        assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트() {
        // given

        // stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit", "spring"));
        books.add(new Book(2L, "test", "boot"));

        when(bookRepository.findAll()).thenReturn(books);

        // when
        final BookListRespDto bookList = bookService.getBookList();

        // then
        assertThat(bookList.getItems().get(0).getTitle()).isEqualTo("junit");
        assertThat(bookList.getItems().get(1).getAuthor()).isEqualTo("boot");
    }

    @Test
    public void 책한건보기_테스트() {
        // given
        Long id = 1L;
        Book book = new Book(1L, "junit", "spring");
        Optional<Book> saved = Optional.of(book);

        // stub
        when(bookRepository.findById(id)).thenReturn(saved);

        // when
        final BookRespDto bookRespDto = bookService.getBook(id);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());
    }

    @Test
    public void 책수정하기_테스트() {
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("msa");
        dto.setAuthor("author");

        // stub
        Book book = new Book(1L, "junit", "spring");
        Optional<Book> saved = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(saved);

        // when
        final BookRespDto bookRespDto = bookService.updateBook(id, dto);

        // then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());

    }
}

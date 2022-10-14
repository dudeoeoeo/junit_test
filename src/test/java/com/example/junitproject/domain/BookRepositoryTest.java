package com.example.junitproject.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test 의 의의
 * 애플리케이션이 커지면 커질수록 여러 기능들이 유기적으로 맞물려있기
 * 마련이며 이 중 어느 하나의 기능을 수정했을 때 어디서 사이드 이펙트가
 * 나올지 알 수 없기 때문에 단위 테스트와 통합 테스트로 테스트 코드를
 * 만들어 둬서 최대한 위험을 방지해야 한다. 또한 테스트 코드를 만들면서
 * 한 개의 함수는 하나의 기능을 담당하도록 역할을 분리시켜야 한다.
 */

@ActiveProfiles("dev")
@DataJpaTest // DB 와 관련된 컴포넌트만 메모리에 로딩
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    public void 파라미터_수_만큼_책_저장(int loop) {
        for (int i = 1; i <= loop; i++) {
            Book book = Book.builder()
                    .title("제목" + i)
                    .author("작가" + i)
                    .build();
            bookRepository.save(book);
        }
    }

    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "책 제목";
        String author = "작가명";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트 실행)
        final Book save = bookRepository.save(book);

        // then (검증)
        assertEquals(title, save.getTitle());
        assertEquals(author, save.getAuthor());
    }

    @Test
    public void 책목록보기_test() {
        // given
        this.파라미터_수_만큼_책_저장(5);
        int size = 5;
        String title = "제목1";
        String author = "작가1";

        // when
        final List<Book> bookList = bookRepository.findAll();

        // then
        assertEquals(size, bookList.size());
        assertEquals(title, bookList.get(0).getTitle());
        assertEquals(author, bookList.get(0).getAuthor());
    }

    @Test
    public void 책한권보기_test() {
        // given
        this.파라미터_수_만큼_책_저장(3);

        Long bookId = 2L;
        String title = "제목2";
        String author = "작가2";

        // when
        final Optional<Book> book = bookRepository.findById(bookId);

        // then
        assertEquals(bookId, book.get().getId());
        assertEquals(title, book.get().getTitle());
        assertEquals(author, book.get().getAuthor());
    }

    @Test
    public void 책수정하기_test() {
        // given
        String title = "원피스";
        String author = "오다";
        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        final Book savedBook = bookRepository.save(book);

        String updateTitle = "블리치";
        String updateAuthor = "모름";

        // when
        savedBook.updateTitle(updateTitle);
        savedBook.updateAuthor(updateAuthor);
        final Book updateBook = bookRepository.save(savedBook);

        // then
        assertEquals(updateTitle, updateBook.getTitle());
        assertEquals(updateAuthor, updateBook.getAuthor());
    }
}

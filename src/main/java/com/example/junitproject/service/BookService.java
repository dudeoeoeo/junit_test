package com.example.junitproject.service;

import com.example.junitproject.domain.Book;
import com.example.junitproject.domain.BookRepository;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto createBook(BookSaveReqDto dto) {
        final Book save = bookRepository.save(dto.toEntity());

        return save.toDto();
    }

    public List<BookRespDto> getBookList() {
        return bookRepository.findAll()
                .stream()
                .map(Book::toDto)
                .collect(Collectors.toList());
    }

    public BookRespDto getBook(Long bookId) {
        final Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty())
            throw new IllegalArgumentException("Book 을 찾을 수 없습니다.");

        return book.get().toDto();
    }

    public BookRespDto updateBook(Long bookId, BookSaveReqDto dto) {
        final Optional<Book> book = bookRepository.findById(bookId);

        if (book.isEmpty())
            throw new IllegalArgumentException("Book 을 찾을 수 없습니다.");

        book.get().updateAuthor(dto.getAuthor());
        book.get().updateTitle(dto.getTitle());

        return book.get().toDto();
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}

package com.example.junitproject.web;

import com.example.junitproject.service.BookService;
import com.example.junitproject.web.dto.response.BookListRespDto;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import com.example.junitproject.web.dto.response.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }

        final BookRespDto saved = bookService.createBook(dto);
        CMRespDto<?> cmRespDto = CMRespDto.builder().code(1).msg("책 저장").body(saved).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED);
    }
    @GetMapping("/book")
    public ResponseEntity<?> getBookList() {
        final BookListRespDto bookList = bookService.getBookList();
        CMRespDto cmRespDto = CMRespDto.builder().code(1).msg("글 목록 가져오기").body(bookList).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.OK);
    }
    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        final BookRespDto book = bookService.getBook(id);
        CMRespDto cmRespDto = CMRespDto.builder().code(1).msg("글 가져오기").body(book).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.OK);
    }
    @PutMapping("/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookSaveReqDto dto,
                                        BindingResult bindingResult)
    {
        final BookRespDto respDto = bookService.updateBook(id, dto);
        CMRespDto cmRespDto = CMRespDto.builder().code(1).msg("글 가져오기").body(respDto).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.OK);
    }
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        CMRespDto cmRespDto = CMRespDto.builder().code(1).msg("글 삭제 완료").body(null).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.OK);
    }
}

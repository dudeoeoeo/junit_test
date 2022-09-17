package com.example.junitproject.web;

import com.example.junitproject.service.BookService;
import com.example.junitproject.web.dto.response.BookRespDto;
import com.example.junitproject.web.dto.request.BookSaveReqDto;
import com.example.junitproject.web.dto.response.CMRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
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
    public ResponseEntity<?> getBookList() {
        return null;
    }
    public ResponseEntity<?> getBook() {
        return null;
    }
    public ResponseEntity<?> updateBook() {
        return null;
    }
    public ResponseEntity<?> deleteBook() {
        return null;
    }
}

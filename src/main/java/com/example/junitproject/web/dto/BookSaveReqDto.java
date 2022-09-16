package com.example.junitproject.web.dto;

import com.example.junitproject.domain.Book;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookSaveReqDto {

    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }

}

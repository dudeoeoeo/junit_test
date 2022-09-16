package com.example.junitproject.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookRespDto {

    private Long id;
    private String title;
    private String author;

    @Builder
    public BookRespDto (Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

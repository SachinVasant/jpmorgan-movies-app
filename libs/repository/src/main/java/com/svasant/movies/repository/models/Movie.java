package com.svasant.movies.repository.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Builder
@Data
@Document
public class Movie {
    @Id
    private String id;

    @Indexed
    private String title;

    @Indexed
    private Integer year;

    @Indexed
    private Set<String> cast;

    @Indexed
    private Set<String> genres;
}

package com.svasant.movies.repository.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document
public class Movie {
    @Id
    private UUID uuid;
    private String title;
    private int year;
    private Set<String> cast;
    private Set<String> genres;

    public Movie() {
        this.uuid = UUID.randomUUID();
    }

    public Movie(String title) {
        this();
        this.title = title;
    }
    
}

package com.svasant.movies.reader.controllers;

import com.svasant.movies.repository.models.Movie;
import com.svasant.movies.reader.models.MoviesResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieReaderController {
    @GetMapping(value = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoviesResponse getMovies() {
        var moviesResponse = new MoviesResponse();
        moviesResponse.setResults(List.of(Movie.builder().title("Harry Potter").build()));
        return moviesResponse;
    }
}

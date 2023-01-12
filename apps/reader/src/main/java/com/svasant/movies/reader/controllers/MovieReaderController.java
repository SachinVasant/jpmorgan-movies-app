package com.svasant.movies.reader.controllers;

import com.svasant.movies.repository.models.Movie;
import com.svasant.movies.repository.models.MoviesResponse;
import com.svasant.movies.repository.query.MovieQuery;
import com.svasant.movies.repository.services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class MovieReaderController {

    @Autowired
    private MovieService movieService;

    @GetMapping(value = "reader/api/v0/movies", produces = MediaType.APPLICATION_JSON_VALUE)
    public MoviesResponse getMovies(
            @RequestParam(required=false) String title,
            @RequestParam(required = false) String cast,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "50") int limit) {
        var movieQuery = MovieQuery.builder()
                .title(title)
                .cast(cast)
                .year(year)
                .genre(genre)
                .offset(offset)
                .limit(limit)
                .build();
        return movieService.search(movieQuery);
    }

    @GetMapping(value = "reader/api/v0/buildMovie", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> buildMovies() {
        List<Movie> moviesToSave = List.of(
                Movie.builder()
                        .title("Harry Potter")
                        .year(2002)
                        .cast(Set.of("Daniel Radcliffe", "Emma Watson"))
                        .genres(Set.of("Fantasy"))
                        .build(),
                Movie.builder()
                        .title("Top Gun")
                        .year(1989)
                        .cast(Set.of("Tom Cruise"))
                        .genres(Set.of("Action"))
                        .build()
        );
        for (Movie movie: moviesToSave) {
            movieService.save(movie);
        }
        return moviesToSave;
    }

}

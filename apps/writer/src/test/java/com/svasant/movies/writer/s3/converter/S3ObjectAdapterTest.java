package com.svasant.movies.writer.s3.converter;

import com.svasant.movies.repository.models.Movie;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class S3ObjectAdapterTest {
    private static final String SAMPLE_S3_JSON_ARRAY = "sample_s3_json_array.json";
    private static final String SAMPLE_S3_OBJECT_JSON = "sample_s3_json_object.json";

    @Test
    public void shouldHandleJsonArrays() throws IOException {
        String sampleJsonArray = new String(getClass().getClassLoader().getResourceAsStream(SAMPLE_S3_JSON_ARRAY).readAllBytes());
        S3ObjectAdapter s3ObjectAdapter = new S3ObjectAdapter();
        List<Movie> expectedMovies = List.of(
                Movie.builder()
                        .title("Iron Man")
                        .year(2008)
                        .cast(Set.of("Robert Downey, Jr."))
                        .genres(Set.of("Action"))
                        .build(),
                Movie.builder()
                        .title("The Avengers")
                        .year(2012)
                        .cast(Set.of("Robert Downey, Jr.", "Chris Evans"))
                        .genres(Set.of("Superhero"))
                        .build()
        );
        assertEquals(s3ObjectAdapter.toMovies(sampleJsonArray), expectedMovies);
    }

    @Test
    public void shouldHandleJsonObjects() throws IOException {
        String sampleJsonObject = new String(getClass().getClassLoader().getResourceAsStream(SAMPLE_S3_OBJECT_JSON).readAllBytes());
        S3ObjectAdapter s3ObjectAdapter = new S3ObjectAdapter();
        List<Movie> expectedMovies = List.of(
                Movie.builder()
                        .title("The Hulk")
                        .year(2012)
                        .cast(Set.of("Mark Ruffalo"))
                        .genres(Set.of("Superhero"))
                        .build()
        );
        assertEquals(s3ObjectAdapter.toMovies(sampleJsonObject), expectedMovies);
    }

    @Test
    public void shouldHandleEmptyJsons() throws IOException {
        String sampleEmptySet = "{}";
        S3ObjectAdapter s3ObjectAdapter = new S3ObjectAdapter();
        List<Movie> expectedMovies = List.of(
                Movie.builder()
                        .title("")
                        .cast(Set.of())
                        .genres(Set.of())
                        .build()
        );
        assertEquals(s3ObjectAdapter.toMovies(sampleEmptySet), expectedMovies);
    }
}
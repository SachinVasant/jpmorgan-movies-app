package com.svasant.movies.repository.query;

import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.junit.jupiter.api.Assertions.*;

class MovieQueryTest {
    @Test
    void shouldBuildQueryWithNoCriteria() {
        assertEquals(MovieQuery.builder().build().toQueryCriteria(), new Criteria());
    }

    @Test
    void shouldMatchByTitle() {
        var title = "Test";
        var movieQuery = MovieQuery.builder()
                .title(title)
                .build();
        var criteria = Criteria.where("title").is(title);

        assertEquals(movieQuery.toQueryCriteria(), criteria);
    }

    @Test
    void shouldMatchByTitleAndCast() {
        var title = "Test";
        var cast = "testcast";
        var movieQuery = MovieQuery.builder()
                .title(title)
                .cast(cast)
                .build();
        var criteria = Criteria.where("title").is(title).and("cast").is(cast);

        assertEquals(movieQuery.toQueryCriteria(), criteria);
    }
    @Test
    void shouldMatchByCastAndYear() {
        var cast = "testcast";
        var year = 2022;
        var movieQuery = MovieQuery.builder()
                .cast(cast)
                .year(year)
                .build();
        var criteria = Criteria.where("cast").is(cast).and("year").is(year);

        assertEquals(movieQuery.toQueryCriteria(), criteria);
    }

}
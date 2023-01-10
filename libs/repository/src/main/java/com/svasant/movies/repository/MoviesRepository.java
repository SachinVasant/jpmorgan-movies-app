package com.svasant.movies.repository;

import com.svasant.movies.repository.models.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MoviesRepository {
    private final MongoTemplate mongoTemplate;

    public Movie save(Movie movie) {
        return mongoTemplate.save(movie);
    }

    public List<Movie> search(Criteria queryCriteria, int offset, int limit) {
        Query query = new Query();
        query.addCriteria(queryCriteria);
        query.skip(offset);
        query.limit(limit);
        return mongoTemplate.find(query, Movie.class);
    }
}

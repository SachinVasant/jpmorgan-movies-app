package com.svasant.movies.repository.services;

import com.svasant.movies.repository.MoviesRepository;
import com.svasant.movies.repository.models.Movie;
import com.svasant.movies.repository.models.MoviesResponse;
import com.svasant.movies.repository.models.PageData;
import com.svasant.movies.repository.query.MovieQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
    @Autowired
    private MoviesRepository moviesRepository;

    public MoviesResponse search(MovieQuery movieQuery) {
        var matchedResults =  moviesRepository.search(movieQuery.toQueryCriteria(), movieQuery.getOffset(), movieQuery.getLimit());
        return MoviesResponse.builder()
                .results(matchedResults)
                .pageData(PageData.builder()
                        .offSet(movieQuery.getOffset())
                        .pageSize(matchedResults.size())
                        .build())
                .build();
    }

    public Movie save(Movie movie) {
        return moviesRepository.save(movie);
    }
}

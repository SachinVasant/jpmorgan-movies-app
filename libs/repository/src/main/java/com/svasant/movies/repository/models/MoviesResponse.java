package com.svasant.movies.repository.models;

import com.svasant.movies.repository.models.Movie;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MoviesResponse {
List<Movie> results;
PageData pageData;
}

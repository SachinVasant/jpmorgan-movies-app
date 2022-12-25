package com.svasant.movies.reader.models;

import com.svasant.movies.repository.models.Movie;
import lombok.Data;

import java.util.List;

@Data
public class MoviesResponse {
List<Movie> results;
}

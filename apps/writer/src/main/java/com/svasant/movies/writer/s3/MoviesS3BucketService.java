package com.svasant.movies.writer.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.svasant.movies.repository.models.Movie;
import com.svasant.movies.writer.s3.converter.S3ObjectAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MoviesS3BucketService {
    private static final String AWS_S3_BUCKET_NAME = "svasant-movies-bucket";

    private final AmazonS3 amazonS3Client;
    private final S3ObjectAdapter s3ObjectAdapter;

    public List<Movie> getMovies(String objectKey) {
        String rawMoviesString = amazonS3Client.getObjectAsString(AWS_S3_BUCKET_NAME, objectKey);
        return s3ObjectAdapter.toMovies(rawMoviesString);
    }
}

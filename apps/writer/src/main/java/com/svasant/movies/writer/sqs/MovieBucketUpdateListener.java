package com.svasant.movies.writer.sqs;

import com.svasant.movies.repository.services.MovieService;
import com.svasant.movies.writer.adapters.S3NotificationEventAdapter;
import com.svasant.movies.writer.s3.MoviesS3BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class MovieBucketUpdateListener {

    private static final String QUEUE = "s3-event-notification-queue";
    private final S3NotificationEventAdapter s3NotificationEventAdapter;
    private final MoviesS3BucketService moviesS3BucketService;
    private final MovieService movieService;

    @SqsListener(value = QUEUE)
    public void onS3BuckentEvent(String s3BucketEvent) {
        Set<String> createdObjectKeys = s3NotificationEventAdapter.toS3ObjectKeys(s3BucketEvent);
        createdObjectKeys.stream()
                .map(moviesS3BucketService::getMovies)
                .flatMap(List::stream)
                .distinct()
                .forEach(movieService::save);
    }
}

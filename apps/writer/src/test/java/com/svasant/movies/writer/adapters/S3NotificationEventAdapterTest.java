package com.svasant.movies.writer.adapters;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class S3NotificationEventAdapterTest {
    private static final String SAMPLE_EVENT_FILE_NAME= "sample_s3_event_notification.json";

    @Test
    public void shouldParseSampleEventToGetObjectKey() throws IOException {
        String sampleEventFile = new String(getClass().getClassLoader().getResourceAsStream(SAMPLE_EVENT_FILE_NAME).readAllBytes());
        S3NotificationEventAdapter eventAdapter = new S3NotificationEventAdapter();
        assertEquals(eventAdapter.toS3ObjectKeys(sampleEventFile), Set.of("test-pineapple"));
    }
}
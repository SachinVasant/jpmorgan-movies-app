package com.svasant.movies.writer.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class S3NotificationEventAdapter {
    public static final String RECORDS = "Records";
    public static final String S_3 = "s3";
    public static final String OBJECT = "object";
    public static final String KEY = "key";

    public Set<String> toS3ObjectKeys(String s3BucketEvent) {
        JsonObject s3EventJson = new JsonParser().parse(s3BucketEvent).getAsJsonObject();
        JsonArray results = s3EventJson.get(RECORDS).getAsJsonArray();
        Set<String> objectKeys = new HashSet<>();
        for (JsonElement result: results) {
            var objectKey = result.getAsJsonObject()
                    .getAsJsonObject(S_3)
                    .getAsJsonObject(OBJECT)
                    .get(KEY)
                    .getAsString();
            objectKeys.add(objectKey);
        }
        return objectKeys;
    }
}

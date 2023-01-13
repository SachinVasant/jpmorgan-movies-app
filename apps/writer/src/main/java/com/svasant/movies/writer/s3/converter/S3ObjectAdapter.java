package com.svasant.movies.writer.s3.converter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.svasant.movies.repository.models.Movie;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class S3ObjectAdapter {

    public List<Movie> toMovies(String s3ObjectContents) {
        JsonElement parsedObjectContents = new JsonParser().parse(s3ObjectContents);
        if (parsedObjectContents.isJsonArray()) {
            List<Movie> movies = new ArrayList<>();
            for (JsonElement parsedObject: parsedObjectContents.getAsJsonArray()) {
                movies.add(toMovie(parsedObject.getAsJsonObject()));
            }
            return movies;
        }
        if (parsedObjectContents.isJsonObject()) {
            return List.of(toMovie(parsedObjectContents.getAsJsonObject()));
        }
        throw new IllegalArgumentException("Cannot parse movies from json " + s3ObjectContents);
    }

    private Movie toMovie(JsonObject jsonObject) {
        return Movie.builder()
                .title(nullSafeGetString(jsonObject, "title"))
                .cast(nullSafeGetSet(jsonObject, "cast"))
                .genres(nullSafeGetSet(jsonObject, "genres"))
                .year(nullSafeGetInt(jsonObject,"year"))
                .build();
    }
    private Set<String> nullSafeGetSet(JsonObject jsonObject, String member) {
        if (!jsonObject.has(member) || !jsonObject.get(member).isJsonArray()) {
            return Set.of();
        }
        Set<String> set = new HashSet<>();
        for (JsonElement setElement: jsonObject.getAsJsonArray(member)) {
            set.add(setElement.getAsString());
        }
        return set;
    }

    private String nullSafeGetString(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key)) {
            return "";
        }
        return jsonObject.get(key).getAsString();
    }

    private Integer nullSafeGetInt(JsonObject jsonObject, String key) {
        if (!jsonObject.has(key)) {
            return null;
        }
        return jsonObject.get(key).getAsInt();
    }
}

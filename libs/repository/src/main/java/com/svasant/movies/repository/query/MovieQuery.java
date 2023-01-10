package com.svasant.movies.repository.query;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

@Builder
public class MovieQuery {
    private String title;

    private String cast;

    private Integer year;

    private  String genre;

    @Getter
    @Builder.Default
    private int offset = 0;

    @Getter
    @Builder.Default
    private int limit = 50;

    /**
     * This metthod builds the query criteria in the order of cardinality, to best use the indexes.
     *
     * @return criteria - for mongoTemplates to consume.
     */
    public Criteria toQueryCriteria() {
        Criteria queryCriteria = new Criteria();
        if (title != null)  {
            queryCriteria.and("title").is(title);
        }
        if (cast != null) {
            queryCriteria.and("cast").is(cast);
        }
        if (year != null) {
            queryCriteria.and("year").is(year);
        }
        if (genre != null) {
            queryCriteria.and("genres").is(genre);
        }
        return queryCriteria;
    }

}

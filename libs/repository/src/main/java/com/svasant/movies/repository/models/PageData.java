package com.svasant.movies.repository.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageData {
    int offSet;
    int pageSize;
}

package com.svasant.movies.reader.models;

import lombok.Data;

@Data
public class PageData {
    int offSet;
    int pageSize;
    String nextPage;
}

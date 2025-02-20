package com.jobnet.common.utils.pagination;

import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaginationRequest {

    @Min(value = 1, message = "{validation.page.min}")
    protected Integer page = 1;

    @Min(value = 1, message = "{validation.pageSize.min}")
    protected Integer pageSize = 10;

    protected List<String> sortBys = List.of("createdAt-desc");
}

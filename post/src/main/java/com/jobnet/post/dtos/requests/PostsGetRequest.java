package com.jobnet.post.dtos.requests;

import com.jobnet.common.utils.pagination.PaginationRequest;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class PostsGetRequest extends PaginationRequest {

    private String search;
    private String categoryId;
    private String professionId;
    private BigInteger minSalary;
    private BigInteger maxSalary;
    private String provinceName;
    private String workingFormat;
    private String recruiterId;
    private String businessId;
    private String activeStatus;
    private List<String> activeStatuses;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate fromDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate toDate;
    private Boolean isExpired;
}

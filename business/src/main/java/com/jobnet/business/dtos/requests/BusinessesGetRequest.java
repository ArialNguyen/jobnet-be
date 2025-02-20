package com.jobnet.business.dtos.requests;

import com.jobnet.common.utils.pagination.PaginationRequest;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class BusinessesGetRequest extends PaginationRequest {

    private String name;
    private String emailDomain;
    private String phone;
    private String status;
    private Boolean isDeleted;
}

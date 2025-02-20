package com.jobnet.user.dtos.requests;

import com.jobnet.common.utils.pagination.PaginationRequest;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class JobSeekersGetRequest extends PaginationRequest {

    private String email;
    private String name;
    private String phone;
    private String accountType;
    private String verificationStatus;
}

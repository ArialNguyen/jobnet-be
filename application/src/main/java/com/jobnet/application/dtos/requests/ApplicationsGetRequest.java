package com.jobnet.application.dtos.requests;

import com.jobnet.application.models.Application;
import com.jobnet.common.utils.pagination.PaginationRequest;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class ApplicationsGetRequest extends PaginationRequest {

    private String jobSeekerId;
    private String recruiterId;
    private Application.EApplicationStatus applicationStatus;
    private List<Application.EApplicationStatus> applicationStatuses;
    private LocalDate fromDate;
    private LocalDate toDate;
}

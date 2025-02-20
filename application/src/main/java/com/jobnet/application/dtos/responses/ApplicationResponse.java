package com.jobnet.application.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.jobnet.application.models.Application;
import com.jobnet.clients.post.PostResponse;
import com.jobnet.clients.resume.ResumeResponse;
import com.jobnet.clients.user.dtos.responses.JobSeekerResponse;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(of = {"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponse {
    private String id;
    private PostResponse post;
    private JobSeekerResponse jobSeeker;
    private ResumeResponse resume;
    private LocalDateTime createdAt;
    private Application.EApplicationStatus applicationStatus;
}

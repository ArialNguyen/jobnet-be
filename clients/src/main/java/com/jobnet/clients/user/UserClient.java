package com.jobnet.clients.user;

import com.jobnet.clients.user.dtos.requests.UpgradeRequest;
import com.jobnet.clients.user.dtos.responses.JobSeekerResponse;
import com.jobnet.clients.user.dtos.responses.RawRecruiterResponse;
import com.jobnet.common.dtos.UserResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user")
public interface UserClient {

    @GetMapping("api/jobSeekers/{id}")
    JobSeekerResponse getJobSeekerById(@PathVariable String id);

    @GetMapping("api/recruiters/{id}/raw")
    RawRecruiterResponse getRawRecruiterById(@PathVariable String id);

    @GetMapping("api/recruiters/{id}/existsById")
    Boolean existsRecruiterById(@PathVariable String id);

    @PostMapping("api/upgrade")
    UserResponse upgradeUser(@RequestBody @Valid UpgradeRequest request);

}

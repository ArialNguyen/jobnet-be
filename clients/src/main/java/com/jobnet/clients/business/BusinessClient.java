package com.jobnet.clients.business;

import com.jobnet.common.dtos.BusinessFollower;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("business")
public interface BusinessClient {

	@GetMapping("api/businesses/{id}")
	BusinessResponse getBusinessById(@PathVariable String id);

	@PostMapping("api/businesses")
	BusinessResponse createBusiness(BusinessRegisterRequest request);

	@PutMapping("api/businesses/{id}/follow")
	public BusinessResponse updateBusinessFollowers(
		@PathVariable String id,
		@RequestBody @Valid BusinessFollower request
	);
}

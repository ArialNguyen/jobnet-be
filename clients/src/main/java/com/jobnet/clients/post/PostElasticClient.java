package com.jobnet.clients.post;

import com.jobnet.common.dtos.PostElasticResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "elastic-service", url = "https://acbd-104-28-205-70.ngrok-free.app", path = "/api/post")
public interface PostElasticClient {
	@PostMapping()
	PostElasticResponse createPostElastic(@RequestBody PostElasticResponse postElasticResponse);
}

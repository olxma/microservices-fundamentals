package com.epam.microservices.resourceprocessor.feign;

import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "songService")
public interface SongServiceClient {

    @PostMapping("/songs")
    void createSongMetadata(@RequestBody AudioFileMetadata metadata);
}

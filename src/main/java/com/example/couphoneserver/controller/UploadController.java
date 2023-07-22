package com.example.couphoneserver.controller;

import com.example.couphoneserver.common.annotation.NoAuth;
import com.example.couphoneserver.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping(value = "/upload")
@RequiredArgsConstructor
public class UploadController {
    private final S3Uploader s3Uploader;

    @NoAuth
    @PostMapping(value="",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String postImage(
            @RequestPart("file") MultipartFile multipartFile) {
        log.error("[UploadController.upload]");
        return s3Uploader.upload(multipartFile);
    }
}
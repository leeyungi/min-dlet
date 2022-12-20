package com.i3e3.mindlet.domain.file.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String s3Upload(MultipartFile multipartFile) throws IOException;
}

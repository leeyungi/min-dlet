package com.i3e3.mindlet.domain.file.service;

import com.i3e3.mindlet.global.util.S3UploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3UploadUtil s3UploadUtil;

    @Value("${path.files.images.content}")
    private String dirPath;

    @Override
    public String s3Upload(MultipartFile multipartFile) throws IOException {
        return s3UploadUtil.upload(multipartFile, dirPath);
    }
}

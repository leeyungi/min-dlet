package com.i3e3.mindlet.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.i3e3.mindlet.global.constant.message.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3UploadUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirPath) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalStateException(ErrorMessage.FILE_CONVERT_FAIL.getMessage()));

        return upload(uploadFile, dirPath);
    }

    private String upload(File uploadFile, String dirPath) {
        String fileName = dirPath + "/" + UUID.randomUUID() + "." + extractExt(uploadFile.getName());
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String filename) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, filename, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, filename).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            return;
        }

        throw new IllegalStateException(ErrorMessage.FILE_DELETE_FAIL.getMessage());
    }

    public Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }

            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}

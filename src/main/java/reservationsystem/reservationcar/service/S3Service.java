package reservationsystem.reservationcar.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;
    private static final String BUCKET_NAME = "pych-car-reservation-bucket";

    public String uploadFile(MultipartFile file, String location) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileUrl = "https://" + BUCKET_NAME + ".s3.ap-northeast-2.amazonaws.com/" + location + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(BUCKET_NAME, fileName, file.getInputStream(), metadata);
        return fileUrl;
    }
}
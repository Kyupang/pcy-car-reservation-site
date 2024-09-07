package reservationsystem.reservationcar.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.domain.WarningImage;
<<<<<<< HEAD
import reservationsystem.reservationcar.service.S3Service;
import reservationsystem.reservationcar.service.WarningImageService;


@Controller
@RequiredArgsConstructor
public class WarningImageController {

    private final WarningImageService warningImageService;
    private final S3Service s3Service;  // S3 서비스 주입
=======
import reservationsystem.reservationcar.service.WarningImageService;

@Controller
@RequiredArgsConstructor
public class WarningImageController {
    private final WarningImageService warningImageService;

    @Value("${upload.path}")
    private String uploadPath;
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c

    // 파일 업로드 폼
    @GetMapping("/warningImageUpload")
    public String uploadForm() {
        return "management/warningImageManagement";
    }

    // 파일 업로드 처리
    @PostMapping("/ImageUpload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
<<<<<<< HEAD
                // S3에 파일 업로드
                String fileUrl = s3Service.uploadFile(file, "warning"); // "warning" 디렉토리에 업로드

                // WarningImage 객체에 S3 URL 저장
                WarningImage warningImage = new WarningImage();
                warningImage.setWarningImageUrl(fileUrl);
                warningImageService.saveOrUpdateWarningImage(warningImage); // DB에 경고 이미지 정보 저장
=======
                // Define the directory path
                Path uploadDir = Paths.get(uploadPath, "warning");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Define the file path
                String fileName = file.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);

                // Save the file
                file.transferTo(filePath);

                // Set file URL
                String fileUrl = "/warning/" + fileName;

                // Save car information with file URL
                WarningImage warningImage = new WarningImage();
                warningImage.setWarningImageUrl(fileUrl);
                warningImageService.saveOrUpdateWarningImage(warningImage); // Save car details to your database

>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
<<<<<<< HEAD
        return "redirect:/"; // 홈 페이지로 리다이렉트
=======
        return "redirect:/"; // Redirect to the home page
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c
    }

    // 업로드된 이미지 조회
    @GetMapping("/viewWarningImage")
    @ResponseBody
    public String getImageUrl() {
        WarningImage warningImage = warningImageService.getWarningImage();
        return warningImage != null ? warningImage.getWarningImageUrl() : "";
    }
}

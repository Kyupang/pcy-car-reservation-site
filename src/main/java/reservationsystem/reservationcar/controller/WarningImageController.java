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
import reservationsystem.reservationcar.service.WarningImageService;

@Controller
@RequiredArgsConstructor
public class WarningImageController {
    private final WarningImageService warningImageService;

    @Value("${upload.path}")
    private String uploadPath;

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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/"; // Redirect to the home page
    }

    // 업로드된 이미지 조회
    @GetMapping("/viewWarningImage")
    @ResponseBody
    public String getImageUrl() {
        WarningImage warningImage = warningImageService.getWarningImage();
        return warningImage != null ? warningImage.getWarningImageUrl() : "";
    }
}

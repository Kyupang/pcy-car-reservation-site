package reservationsystem.reservationcar.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import reservationsystem.reservationcar.service.S3Service;  // S3 서비스 추가

@Controller
@RequiredArgsConstructor
public class TestController {

    private final S3Service s3Service;

    @PostMapping("test")
    public String registerCar(@RequestParam("image") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String fileUrl = s3Service.uploadFile(file, "test");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/"; // 홈 페이지로 리다이렉트
    }
}

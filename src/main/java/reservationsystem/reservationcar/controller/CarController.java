package reservationsystem.reservationcar.controller;

import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reservationsystem.reservationcar.DTO.CarRequestDTO;
import reservationsystem.reservationcar.DTO.CarResponseDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.service.CarService;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reservationsystem.reservationcar.DTO.CarRequestDTO;
import reservationsystem.reservationcar.DTO.CarResponseDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.service.CarService;
import reservationsystem.reservationcar.service.S3Service;  // S3 서비스 추가
import org.springframework.beans.factory.annotation.Value;

=======
import org.springframework.beans.factory.annotation.Value;
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c
@Controller
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
<<<<<<< HEAD
    private final S3Service s3Service;
=======
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c

    @GetMapping("/carManagement")
    public String carManagementPage(Model model) {
        model.addAttribute("carRequestDTO", new CarRequestDTO());
        return "management/carManagement";
    }
<<<<<<< HEAD

=======
    // 차량 등록 서비스
    @Value("${upload.path}")
    private String uploadPath;
>>>>>>> 63d6fc3860df9f039783037c1b2909d5e77ffd2c
    @PostMapping("/car/new")
    public String registerCar(@RequestParam("name") String name,
                              @RequestParam("carNumber") String carNumber,
                              @RequestParam("image") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
<<<<<<< HEAD
                // S3에 파일 업로드
                String fileUrl = s3Service.uploadFile(file, "car");  // "car" 디렉토리로 업로드

                // 차량 정보 저장
                Car car = new Car();
                car.setName(name);
                car.setCarNumber(carNumber);
                car.setCarImageUrl(fileUrl);  // S3 파일 URL 저장
                carService.join(car); // 차량 정보를 DB에 저장
=======
                // Define the directory path
                Path uploadDir = Paths.get(uploadPath, "car");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                // Define the file path
                String fileName = file.getOriginalFilename();
                Path filePath = uploadDir.resolve(fileName);

                // Save the file
                file.transferTo(filePath);

                // Set file URL
                String fileUrl = "/car/" + fileName;

                // Save car information with file URL
                Car car = new Car();
                car.setName(name);
                car.setCarNumber(carNumber);
                car.setCarImageUrl(fileUrl);
                carService.join(car); // Save car details to your database

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

    @GetMapping("/car/getCarList")
    @ResponseBody
    public List<CarResponseDTO> getCarList() {
        List<Car> cars = carService.findCars(); // 모든 차량을 조회하는 서비스 메서드
        return cars.stream()
                .map(car -> new CarResponseDTO(car.getId(), car.getName(), car.getCarImageUrl()))
                .collect(Collectors.toList());
    }
}

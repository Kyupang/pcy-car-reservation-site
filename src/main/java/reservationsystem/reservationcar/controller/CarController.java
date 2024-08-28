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
import org.springframework.beans.factory.annotation.Value;
@Controller
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/carManagement")
    public String carManagementPage(Model model) {
        model.addAttribute("carRequestDTO", new CarRequestDTO());
        return "management/carManagement";
    }
    // 차량 등록 서비스
    @Value("${upload.path}")
    private String uploadPath;
    @PostMapping("/car/new")
    public String registerCar(@RequestParam("name") String name,
                              @RequestParam("carNumber") String carNumber,
                              @RequestParam("image") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
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

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/"; // Redirect to the home page
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

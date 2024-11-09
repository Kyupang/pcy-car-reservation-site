package reservationsystem.reservationcar.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.DTO.CarRequestDTO;
import reservationsystem.reservationcar.DTO.CarResponseDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.service.CarService;

@Controller
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/carManagement")
    public String carManagementPage(Model model) {
        model.addAttribute("carRequestDTO", new CarRequestDTO());
        return "management/carManagement";
    }

    @PostMapping("/car/new")
    public String registerCar(@ModelAttribute CarRequestDTO carRequestDTO,
                              @RequestParam("image") MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                carService.registerCar(carRequestDTO, file); // 서비스 레이어에 차량 등록 로직 위임
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/"; // 홈 페이지로 리다이렉트
    }

    @GetMapping("/car/getCarList")
    @ResponseBody
    // 불필요한 정보는 배제한다.
    public List<CarResponseDTO> getCarList() {
        List<Car> cars = carService.findAllCars();
        return cars.stream()
                .map(car -> new CarResponseDTO(car.getId(), car.getCarName(), car.getCarImageUrl()))
                .collect(Collectors.toList());
    }
}

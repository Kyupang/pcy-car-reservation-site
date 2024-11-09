package reservationsystem.reservationcar.service;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reservationsystem.reservationcar.dto.CarRequestDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.repository.CarRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final S3Service s3Service;

    @Transactional
    public Long registerCar(CarRequestDTO carRequestDTO, MultipartFile file) throws IOException {
        String fileUrl = s3Service.uploadFile(file, "car"); // S3에 파일 업로드

        Car car = new Car();
        car.setCarName(carRequestDTO.getCarName());
        car.setCarNumber(carRequestDTO.getCarNumber());
        car.setCarImageUrl(fileUrl); // S3 파일 URL 저장

        carRepository.save(car); // 차량 정보 저장 후 저장된 Car 객체 반환
        return car.getId();
    }

    public List<Car> findAllCars() {
        return carRepository.findAll();
    }
}

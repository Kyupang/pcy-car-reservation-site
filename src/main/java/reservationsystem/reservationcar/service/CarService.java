package reservationsystem.reservationcar.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.repository.CarRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    @Transactional
    public Long join(Car car) {
        carRepository.save(car);
        return car.getId();
    }

    public List<Car> findCars() {
        return carRepository.findAll();
    }
}

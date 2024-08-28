package reservationsystem.reservationcar.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.domain.Car;

@SpringBootTest
@Transactional
public class CarRepositoryTest {
    @Autowired CarRepository carRepository;

    @Test
    public void 차량_등록() throws Exception {
        //given
        Car car1 = new Car();
        car1.setName("모하비");
        car1.setCarNumber("39러4141");
        //when
        carRepository.save(car1);
        //then

        Assertions.assertThat(car1.getId()).isEqualTo(1L);
    }
    
    @Test
    public void 차량_조회() throws Exception {
        //given
        Car car1 = new Car();
        car1.setName("모하비");
        car1.setCarNumber("39러4141");
        //when
        carRepository.save(car1);
        Car findCar = carRepository.findOne(1L);
        //then
        Assertions.assertThat(car1.getName()).isEqualTo(findCar.getName());
    }
}

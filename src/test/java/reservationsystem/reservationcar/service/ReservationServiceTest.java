package reservationsystem.reservationcar.service;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.DTO.ReservationRequestDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.domain.Reservation;
import reservationsystem.reservationcar.domain.ReservationStatus;
import reservationsystem.reservationcar.repository.CarRepository;
import reservationsystem.reservationcar.repository.ReservationRepository;

@SpringBootTest
@Transactional
public class ReservationServiceTest {

    @Autowired
    EntityManager em;
    @Autowired ReservationService reservationService;
    @Autowired
    ReservationRepository reservationRepository;

    @Test
    public void 자동차_예약() throws Exception {
        //given
        Car car = createCar("스타리아", "69러9271");
        ReservationRequestDTO requestDTO = new ReservationRequestDTO();
        requestDTO.setName("김규환");
        requestDTO.setPhoneNumber("01022342222");
        requestDTO.setAffiliation("청년부");
        requestDTO.setStartTime(LocalDateTime.now());
        requestDTO.setEndTime(LocalDateTime.now());

        //when
        //Long reservationId = reservationService.createReservation(car.getId(), requestDTO);

        //then
        //Reservation getReservation = reservationRepository.findOne(reservationId);

        //Assertions.assertThat(ReservationStatus.REQUESTED).isEqualTo(getReservation.getReservationStatus());
    }

    private Car createCar(String name, String carNumber) {
        Car car = new Car();
        car.setName(name);
        car.setCarNumber(carNumber);

        em.persist(car);
        return car;
    }
}

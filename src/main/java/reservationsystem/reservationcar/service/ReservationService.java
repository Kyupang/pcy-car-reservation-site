package reservationsystem.reservationcar.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.DTO.ReservationRequestDTO;
import reservationsystem.reservationcar.domain.Car;
import reservationsystem.reservationcar.domain.Reservation;
import reservationsystem.reservationcar.domain.ReservationStatus;
import reservationsystem.reservationcar.repository.CarRepository;
import reservationsystem.reservationcar.repository.ReservationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;

    @Transactional
    public ResponseEntity<?> createReservation(Long carId, ReservationRequestDTO requestDTO) {
        Car car = carRepository.findOne(carId);

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        reservation.setName(requestDTO.getName());
        reservation.setPhoneNumber(requestDTO.getPhoneNumber());
        reservation.setDriver(requestDTO.getDriver());
        reservation.setAffiliation(requestDTO.getAffiliation());
        reservation.setPurpose(requestDTO.getPurpose());
        reservation.setNumberOfPassengers(requestDTO.getNumberOfPassengers());
        reservation.setStartTime(requestDTO.getStartTime());
        reservation.setEndTime(requestDTO.getEndTime());
        reservation.setReservationTime(LocalDateTime.now().withNano(0));
        reservation.setReservationStatus(ReservationStatus.REQUESTED);

        reservationRepository.save(reservation);

        // 예약 성공 시 200 OK와 예약 ID를 반환
        // 궁금한점 200을 보내지 않으면 다른 코드가 반환되나 400, 404 등?
        // reservationId": [예약 ID]
        return ResponseEntity.ok(Collections.singletonMap("reservationId", reservation.getId()));
    }

    public boolean isTimeOverlap(Long carId, LocalDateTime startTime, LocalDateTime endTime) {
        boolean hasOverlap = reservationRepository.existsByCarIdAndTimeOverlap(carId, startTime, endTime);

        return hasOverlap;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        reservation.cancel();
    }

    @Transactional
    public void approveReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findOne(reservationId);
        reservation.approve();
    }


    public List<Reservation> getReservationResultsById(Long carId) {
        return reservationRepository.findByCarId(carId);
    }

    public List<Reservation> getReservationResults() {
        return reservationRepository.findAll();
    }

    public Page<Reservation> getReservationResults(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
}

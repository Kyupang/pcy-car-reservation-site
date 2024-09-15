package reservationsystem.reservationcar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    private String name;

    private String phoneNumber;

    private String driver;

    private String affiliation;

    private String purpose;

    private String numberOfPassengers;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    private LocalDateTime reservationTime;

    public void setCar(Car car) {
        this.car = car;
        car.getReservations().add(this);
    }

    public void cancel() {
        if (this.getReservationStatus() == ReservationStatus.APPROVED) {
            throw new IllegalStateException("이미 확정된 예약은 취소가 불가능 합니다.");
        }

        this.setReservationStatus(ReservationStatus.CANCEL);
    }

    public void approve() {
        if (this.getReservationStatus() == ReservationStatus.CANCEL) {
            throw new IllegalStateException("이미 취소된 예약은 확정이 불가능 합니다.");
        }

        this.setReservationStatus(ReservationStatus.APPROVED);
    }
}

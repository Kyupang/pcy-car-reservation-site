package reservationsystem.reservationcar.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ReservationRequestDTO {

    @NotEmpty(message = "이름 입력은 필수입니다")
    private String name;

    @NotEmpty(message = "번호 입력은 필수입니다")
    private String phoneNumber;

    private String driver;

    @NotEmpty(message = "소속 입력은 필수입니다")
    private String affiliation;

    @NotEmpty(message = "목적지 및 목적 입력은 필수입니다")
    private String purpose;

    @NotEmpty(message = "탑승인원 입력은 필수입니다")
    private String numberOfPassengers;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    private Long carId;
}

package reservationsystem.reservationcar.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    private Long carId;
}

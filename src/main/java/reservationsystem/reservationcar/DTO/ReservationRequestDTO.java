package reservationsystem.reservationcar.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequestDTO {

    @NotEmpty(message = "이름 입력은 필수입니다")
    private String name;

    @NotEmpty(message = "번호 입력은 필수입니다")
    private String phoneNumber;

    @NotEmpty(message = "소속 입력은 필수입니다")
    private String affiliation;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
}

package reservationsystem.reservationcar.DTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationResultDTO {
    private String carName;
    private LocalDateTime start;
    private LocalDateTime end;
}

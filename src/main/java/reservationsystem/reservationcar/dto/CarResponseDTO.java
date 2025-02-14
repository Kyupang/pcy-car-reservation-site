package reservationsystem.reservationcar.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarResponseDTO {
    private Long id;
    private String carName;
    private String carImageUrl;
}
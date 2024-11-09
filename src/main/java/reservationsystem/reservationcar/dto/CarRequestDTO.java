package reservationsystem.reservationcar.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CarRequestDTO {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String carName;
    @NotEmpty(message = "차량 넘버는 필수 입니다")
    private String carNumber;
}

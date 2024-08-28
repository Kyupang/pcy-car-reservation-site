package reservationsystem.reservationcar.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarRequestDTO {

    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    @NotEmpty(message = "차량 넘버는 필수 입니다")
    private String carNumber;

    private String imageUrl;
}

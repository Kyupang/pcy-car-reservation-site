package reservationsystem.reservationcar.DTO;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private String author;
    private List<String> imageUrls;
    private LocalDateTime timestamp;
}
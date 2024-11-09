package reservationsystem.reservationcar.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private String author;
    private List<String> imageUrls;
    private LocalDateTime timestamp;
}
package reservationsystem.reservationcar.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import reservationsystem.reservationcar.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoardType(String boardType);
}
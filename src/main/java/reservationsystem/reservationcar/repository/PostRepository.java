package reservationsystem.reservationcar.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import reservationsystem.reservationcar.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByBoardType(String boardType, Pageable pageable);
}
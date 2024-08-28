package reservationsystem.reservationcar.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import reservationsystem.reservationcar.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}


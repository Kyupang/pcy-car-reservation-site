package reservationsystem.reservationcar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Long id;

    private String carName;

    private String carNumber;

    private String carImageUrl;

    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations = new ArrayList<>();

}

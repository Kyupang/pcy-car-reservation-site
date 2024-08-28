package reservationsystem.reservationcar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Car {
    @Id @GeneratedValue
    @Column(name = "car_id")
    private Long id;

    private String name;

    private String carNumber;

    private String carImageUrl;

    @OneToMany(mappedBy = "car")
    private List<Reservation> reservations = new ArrayList<>();
}

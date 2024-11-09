package reservationsystem.reservationcar.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reservationsystem.reservationcar.domain.Car;

@Repository
@RequiredArgsConstructor
public class CarRepository {
    private final EntityManager em;

    public void save(Car car) {
        em.persist(car);
    }

    public Car findOne(Long id) {
        return em.find(Car.class, id);
    }

    public List<Car> findAll() {
        return em.createQuery("select c from Car c", Car.class)
                .getResultList();
    }

//    public Car findByName(String name) {
//        return em.createQuery("select c from Car c where c.name = :name", Car.class)
//                .setParameter("name", name)
//                .getResultList();
//    }

//    public void delete(Long id) {
//        Car car = em.find(Car.class, id);
//
//        if (car != null) {
//            em.remove(car);
//        }
//    }
}

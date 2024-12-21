package reservationsystem.reservationcar.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reservationsystem.reservationcar.domain.Reservation;
import reservationsystem.reservationcar.domain.ReservationStatus;

@Repository
@RequiredArgsConstructor
public class ReservationRepository {
    private final EntityManager em;

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public Reservation findOne(Long id) {
        return em.find(Reservation.class, id);
    }

    public List<Reservation> findByCarId(Long carId) {
        return em.createQuery(
                        "select r from Reservation r where r.car.id = :carId and r.reservationStatus != :status order by r.id desc",
                        Reservation.class)
                .setParameter("carId", carId)
                .setParameter("status", ReservationStatus.CANCEL)
                .getResultList();
    }

    // 예약 캘린더를 위한 find
    public List<Reservation> findAll() {
        return em.createQuery(
                        "select r from Reservation r where r.reservationStatus != :status order by r.id desc",
                        Reservation.class)
                .setParameter("status", ReservationStatus.CANCEL)
                .getResultList();
    }

    // 예약 목록을 위한 find
    public Page<Reservation> findAll(Pageable pageable) {
        String jpql = "SELECT r FROM Reservation r order by r.id desc";
        TypedQuery<Reservation> query = em.createQuery(jpql, Reservation.class);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int firstResult = pageNumber * pageSize;

        query.setFirstResult(firstResult);
        query.setMaxResults(pageSize);

        List<Reservation> reservations = query.getResultList();

        TypedQuery<Long> countQuery = em.createQuery("SELECT COUNT(r) FROM Reservation r", Long.class);
        Long total = countQuery.getSingleResult();

        return new PageImpl<>(reservations, pageable, total);
    }


    public boolean existsByCarIdAndTimeOverlap(Long carId, LocalDateTime startTime, LocalDateTime endTime) {
        Long count = em.createQuery(
                        "SELECT COUNT(r) FROM Reservation r WHERE r.car.id = :carId AND r.startTime < :endTime AND r.endTime > :startTime AND r.reservationStatus <> 'cancel'",
                        Long.class)
                .setParameter("carId", carId)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getSingleResult(); //getSingleResult() 카운트 쿼리나 단일 결과 조회에 사용.

        return count > 0;
    }
}

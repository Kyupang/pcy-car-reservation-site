package reservationsystem.reservationcar.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.domain.WarningImage;

@Repository
@RequiredArgsConstructor
public class WarningImageRepository {
    private final EntityManager em;

    @Transactional
    public void save(WarningImage warningImage) {
        List<WarningImage> existingImages = em.createQuery("SELECT w FROM WarningImage w", WarningImage.class).getResultList();

        if (existingImages.isEmpty()) {
            // 기존 이미지가 없으면 새로 저장
            em.persist(warningImage);
        } else {
            // 기존 이미지가 있으면 덮어쓰기 (업데이트)
            WarningImage existingImage = existingImages.get(0); // 첫 번째 이미지만 사용
            existingImage.setWarningImageUrl(warningImage.getWarningImageUrl());
            em.merge(existingImage);
        }
    }

    // WarningImage를 조회하는 메서드
    public WarningImage find() {
        List<WarningImage> existingImages = em.createQuery("SELECT w FROM WarningImage w", WarningImage.class).getResultList();
        return existingImages.isEmpty() ? null : existingImages.get(0);
    }

}

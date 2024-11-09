package reservationsystem.reservationcar.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reservationsystem.reservationcar.domain.WarningImage;
import reservationsystem.reservationcar.repository.WarningImageRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WarningImageService {
    private final WarningImageRepository warningImageRepository;

    // WarningImage 저장 서비스 메서드
    @Transactional
    public void saveOrUpdateWarningImage(WarningImage warningImage) {
        warningImageRepository.save(warningImage);
    }

    // WarningImage 조회 서비스 메서드
    public WarningImage getWarningImage() {
        return warningImageRepository.find();
    }
}

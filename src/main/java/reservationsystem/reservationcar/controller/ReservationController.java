package reservationsystem.reservationcar.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import reservationsystem.reservationcar.DTO.ReservationRequestDTO;
import reservationsystem.reservationcar.DTO.ReservationResultDTO;
import reservationsystem.reservationcar.domain.Reservation;
import reservationsystem.reservationcar.domain.ReservationStatus;
import reservationsystem.reservationcar.service.ReservationService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReservationController {
    private final ReservationService reservationService;

    // 맵핑 매서드
    @GetMapping("/managerLogin")
    public String toMoveLoginPage() {
        return "management/managerLogin";
    }

    @GetMapping("/management")
    public String toMoveManagementPage() {
        return "management/management";
    }

    @GetMapping("/reservation/new") // 예약 시스템
    public String createForm(Model model, @RequestParam(value = "carId", required = false) Long carId) {
        model.addAttribute("carId", carId);
        model.addAttribute("reservationRequestDTO", new ReservationRequestDTO());
        return "reservation/createReservationForm";
    }

    @GetMapping("/reservation/confirm") // 예약 확인 페이지
    public String confirmReservation(
            @RequestParam("carId") Long carId,
            @RequestParam("name") String name,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("affiliation") String affiliation,
            @RequestParam("purpose") String purpose,
            @RequestParam("numberOfPassengers") String numberOfPassengers,
            @RequestParam("startTime") LocalDateTime startTime,
            @RequestParam("endTime") LocalDateTime endTime,
            Model model) {

        ReservationRequestDTO requestDTO = new ReservationRequestDTO();
        requestDTO.setName(name);
        requestDTO.setPhoneNumber(phoneNumber);
        requestDTO.setAffiliation(affiliation);
        requestDTO.setPurpose(purpose);
        requestDTO.setNumberOfPassengers(numberOfPassengers);
        requestDTO.setStartTime(startTime);
        requestDTO.setEndTime(endTime);

        model.addAttribute("reservationRequestDTO", requestDTO);
        model.addAttribute("carId", carId);

        return "reservation/confirmReservation";
    }

    @PostMapping("/reservation/complete") // 예약 확인페이지에서 확인을 누르면 예약 확정
    public String completeReservation(@ModelAttribute ReservationRequestDTO requestDTO,
                                      @RequestParam("carId") Long carId) {
        reservationService.createReservation(carId, requestDTO);
        return "redirect:/";
    }

    // 예약 시스템
    @PostMapping("/reservation/new")
    @ResponseBody // 예약 만들기, 오류없이 예약 만들기
    public ResponseEntity<Map<String, Object>> create(@ModelAttribute @Valid ReservationRequestDTO requestDTO,
                                                      @RequestParam("carId") Long carId, BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("errorMessage", "모든 필드를 올바르게 입력해 주세요.");
            return ResponseEntity.ok(response);
        }

        if (reservationService.isTimeOverlap(carId, requestDTO.getStartTime(), requestDTO.getEndTime())) {
            response.put("success", false);
            response.put("errorMessage", "선택한 시간에 이미 예약이 존재합니다. 다른 시간을 선택해 주세요.");
            return ResponseEntity.ok(response);
        }

        try {
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("errorMessage", e.getMessage());
            return ResponseEntity.ok(response);
        }

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservation/getReservationListById")
    @ResponseBody  //예약시 차량 id별 예약 내용 확인하기
    public List<ReservationResultDTO> getReservationById(@RequestParam Long carId) {
        List<Reservation> reservations = reservationService.getReservationResultsById(carId);
        return reservations.stream()
                .map(reservation -> new ReservationResultDTO(
                        reservation.getCar().getName(),
                        reservation.getAffiliation(),
                        reservation.getStartTime(),
                        reservation.getEndTime()))
                .collect(Collectors.toList());
    }
    
    
    // 캘린더 페이지
    @GetMapping("/reservation/calendar")
    public String calendar() {
        return "calendar/calendarReservation";
    }

    @GetMapping("/reservation/getReservationList")
    @ResponseBody // 예약 캘린더에서 모든 예약 가지고오기
    public List<ReservationResultDTO> getReservations() {
        List<Reservation> reservations = reservationService.getReservationResults();
        return reservations.stream()
                .map(reservation -> new ReservationResultDTO(
                        reservation.getCar().getName(),
                        reservation.getAffiliation(),
                        reservation.getStartTime(),
                        reservation.getEndTime()))
                .collect(Collectors.toList());
    }
    
    // 예약 목록 페이지
    @GetMapping("/reservation/textList")
    public String getReservationsTextList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("reservationTime").descending());
        Page<Reservation> reservationPage = reservationService.getReservationResults(pageable);

        model.addAttribute("reservationTextList", reservationPage.getContent());
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "reservation/reservationList";
    }
    

    // 예약 확정 목록 페이지
    @GetMapping("/reservation/management")
    public String getReservationsTextListForManagement(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("reservationTime").descending());
        Page<Reservation> reservationPage = reservationService.getReservationResults(pageable);

        model.addAttribute("reservationTextList", reservationPage.getContent());
        model.addAttribute("totalPages", reservationPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "management/reservationManagement";
    }

    // 예약 취소 메서드
    @PostMapping("/reservation/{id}/cancel")
    public String cancelReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("message", "Reservation cancelled successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservation/management";
    }

    // 예약 승인 메서드
    @PostMapping("/reservation/{id}/approve")
    public String approveReservation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.approveReservation(id);
            redirectAttributes.addFlashAttribute("message", "Reservation approved successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservation/management";
    }
}

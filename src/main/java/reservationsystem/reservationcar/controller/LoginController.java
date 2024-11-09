package reservationsystem.reservationcar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reservationsystem.reservationcar.DTO.LoginRequestDTO;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/management")
    public String toMoveManagementPage() {
        return "management/management";
    }

    @GetMapping("/managerLogin")
    public String toMoveLoginPage() {
        return "management/managerLogin";
    }

    @PostMapping("/management/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        // 비밀번호 검증 로직
        if (isValidPassword(loginRequestDTO.getPassword())) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 잘못되었습니다.");
        }
    }

    private boolean isValidPassword(String password) {
        return "1225".equals(password);
    }
}

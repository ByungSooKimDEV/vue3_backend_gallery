package gallery.backend.controller;

import io.jsonwebtoken.Claims;
import gallery.backend.domain.item.Member;
import gallery.backend.repository.MemberRepository;
import gallery.backend.service.JwtService;
import gallery.backend.service.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @PostMapping("/api/account/login")
    public ResponseEntity login(@RequestBody Map<String, String> params,
                                HttpServletResponse res) throws IOException {
        Member member = memberRepository.findMemberByEmailAndPassword(params.get("email"), params.get("password"));

        // member값이 null이 아니면 id 값을 token화해서 token을 Cookie에 넣은 다음에 응답값에 추가해준다.
        // token을 return 해서 클라이언트에서 sessionStorage나 Cookie에서 관리할 수도 있지만,
        // Server-Side 에서 관리를 하는게 더 안전하다.
        if(member != null) {
            JwtService jwtService = new JwtServiceImpl();
            int id = member.getId();
            String token = jwtService.getToken("id", id);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");

            res.addCookie(cookie);

//            return ResponseEntity.ok().build();
            return new ResponseEntity<>(id, HttpStatus.OK);

        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/api/account/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String token) throws IOException {
        Claims claims = jwtService.getClaims(token);

        if(claims != null) {
            int id = Integer.parseInt(claims.get("id").toString());
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}

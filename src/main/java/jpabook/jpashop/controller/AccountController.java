package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.Member;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.JwtService;
import jpabook.jpashop.service.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final MemberRepository memberRepository;

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

}

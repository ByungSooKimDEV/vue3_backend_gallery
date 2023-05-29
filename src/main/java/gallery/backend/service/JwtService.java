package gallery.backend.service;

import io.jsonwebtoken.Claims;

import java.io.IOException;

public interface JwtService {
    String getToken(String key, Object value) throws IOException;

    Claims getClaims(String token) throws IOException;

    boolean isValid(String token) throws IOException; // 인자로 받은 토큰이 문제가 없는지...

    int getId(String token) throws IOException; // 토큰에서 사용자의 아이디를 가져옴...
}

package gallery.backend.service;

import io.jsonwebtoken.Claims;

import java.io.IOException;

public interface JwtService {
    String getToken(String key, Object value) throws IOException;

    Claims getClaims(String token) throws IOException;

}

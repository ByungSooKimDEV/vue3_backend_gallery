package gallery.backend.service;

import io.jsonwebtoken.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtService")
public class JwtServiceImpl implements JwtService{

    // 절대 외부에 노출되면 안되는 값이다.
    private static final String SECRET_KEY_FILE = "jwt-secret.key";

    @Override
    public String getToken(String key, Object value) throws IOException {

        String secretKey = getSecretKey();
        System.out.println("secretKey = " + secretKey);

        // 토큰 유효 시간
        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + 1000 * 60 * 5 ); // 5분...
        byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
        Key signKey  = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder jwtBuilder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return jwtBuilder.compact();


    }

    @Override
    public Claims getClaims(String token) throws IOException {
        if(token != null && !"".equals(token))
        {
            try{
                String secretKey = getSecretKey();

                byte[] secretByteKey = DatatypeConverter.parseBase64Binary(secretKey);
                Key signKey  = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            } catch (ExpiredJwtException e) {
                // 만료됨
            } catch (JwtException e) {
                // 유효하지 않음
            }

        }
        return null;
    }

    private String getSecretKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(SECRET_KEY_FILE);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}

package jpabook.jpashop.service;

import java.io.IOException;

public interface JwtService {
    public String getToken(String key, Object value) throws IOException;

}

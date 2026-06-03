package com.suppliers_tgs_api.utils;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;

import java.text.ParseException;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class JwtDecoderUtil {


    public static UUID getUserId(String token) {
        try {
            JWTClaimsSet claims = JWTParser.parse(token).getJWTClaimsSet();

            String id = claims.getStringClaim("id");
            return UUID.fromString(id);

        } catch (ParseException e) {
            throw new RuntimeException("Token JWT inválido", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("El claim 'id' no es un UUID válido", e);
        }
    }
}